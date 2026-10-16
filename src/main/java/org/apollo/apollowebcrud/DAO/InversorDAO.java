package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.Inversor;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InversorDAO extends ApolloConnection {

    public InversorDAO() {
        super();
    }

    private Inversor montar(ResultSet rs) throws SQLException {
        Inversor inversor = new Inversor();
        inversor.setId(rs.getInt("id"));
        inversor.setIdFilial(rs.getInt("id_filial"));
        inversor.setCodigo(rs.getString("codigo"));
        inversor.setNumeroSerie(rs.getString("numero_serie"));
        inversor.setMarca(rs.getString("marca"));
        inversor.setModelo(rs.getString("modelo"));
        inversor.setPotenciaKw(rs.getDouble("potencia_kw"));
        inversor.setStatus(StatusGeral.valueOf(rs.getString("status")));
        inversor.setDataInstalacao(rs.getDate("data_instalacao").toLocalDate());
        return inversor;
    }

// CREATE

    public int inserir(Inversor inversor) {
        String sql = "INSERT INTO inversor (id_filial, codigo, numero_serie, marca, modelo, potencia_kw, status, data_instalacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // retorna o id gerado pelo banco
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, inversor.getIdFilial());
            stmt.setString(2, inversor.getCodigo());
            stmt.setString(3, inversor.getNumeroSerie());
            stmt.setString(4, inversor.getMarca());
            stmt.setString(5, inversor.getModelo());
            stmt.setDouble(6, inversor.getPotenciaKw());
            stmt.setString(7, StatusGeral.ATIVO.name());
            stmt.setDate(8, Date.valueOf(inversor.getDataInstalacao()));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return -1;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir inversor: " + e.getMessage());
            return -1;
        }
    }

    // READ por id

    public Inversor buscarPorId(int id) {
        String sql = "SELECT * FROM inversor WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar inversor: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    public List<Inversor> listarTodos() {
        List<Inversor> lista = new ArrayList<>();
        String sql = "SELECT * FROM inversor ORDER BY codigo";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar inversores: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    public boolean atualizar(Inversor inversor) {
        String sql = "UPDATE inversor SET id_filial = ?, codigo = ?, numero_serie = ?, marca = ?, modelo = ?, potencia_kw = ?, status = ?, data_instalacao = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, inversor.getIdFilial());
            stmt.setString(2, inversor.getCodigo());
            stmt.setString(3, inversor.getNumeroSerie());
            stmt.setString(4, inversor.getMarca());
            stmt.setString(5, inversor.getModelo());
            stmt.setDouble(6, inversor.getPotenciaKw());
            stmt.setString(7, inversor.getStatus().name());
            stmt.setDate(8, Date.valueOf(inversor.getDataInstalacao()));
            stmt.setInt(9, inversor.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar inversor: " + e.getMessage());
            return false;
        }
    }

    // DELETE

    public boolean excluir(int id) {
        String sql = "DELETE FROM inversor WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir inversor: " + e.getMessage());
            return false;
        }
    }
}