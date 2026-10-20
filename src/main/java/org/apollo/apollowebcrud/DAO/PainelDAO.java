package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Painel;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PainelDAO extends ApolloConnection {

    public PainelDAO() {
        super();
    }

    private Painel montar(ResultSet rs) throws SQLException {
        Painel painel = new Painel();
        painel.setId(rs.getInt("id"));
        painel.setIdString(rs.getInt("id_string"));
        painel.setNumeroSerie(rs.getString("numero_serie"));
        painel.setMarca(rs.getString("marca"));
        painel.setModelo(rs.getString("modelo"));
        painel.setPotenciaW(rs.getDouble("potencia_w"));
        painel.setNotaFiscal(rs.getString("nota_fiscal"));
        painel.setStatus(StatusGeral.valueOf(rs.getString("status")));
        painel.setDataInstalacao(rs.getDate("data_instalacao").toLocalDate());
        return painel;
    }

// CREATE

    public int inserir(Painel painel) {
        String sql = "INSERT INTO painel (id_string, numero_serie, marca, modelo, potencia_w, nota_fiscal, status, data_instalacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // retorna o id gerado pelo banco
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, painel.getIdString());
            stmt.setString(2, painel.getNumeroSerie());
            stmt.setString(3, painel.getMarca());
            stmt.setString(4, painel.getModelo());
            stmt.setDouble(5, painel.getPotenciaW());
            stmt.setString(6, painel.getNotaFiscal());
            stmt.setString(7, StatusGeral.ATIVO.name());
            stmt.setDate(8, Date.valueOf(painel.getDataInstalacao()));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return -1;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir painel: " + e.getMessage());
            return -1;
        }
    }

    // READ por id

    public Painel buscarPorId(int id) {
        String sql = "SELECT * FROM painel WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar painel: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    public List<Painel> listarTodos() {
        List<Painel> lista = new ArrayList<>();
        String sql = "SELECT * FROM painel ORDER BY marca, modelo";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar painéis: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    public boolean atualizar(Painel painel) {
        String sql = "UPDATE painel SET id_string = ?, numero_serie = ?, marca = ?, modelo = ?, potencia_w = ?, nota_fiscal = ?, status = ?, data_instalacao = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, painel.getIdString());
            stmt.setString(2, painel.getNumeroSerie());
            stmt.setString(3, painel.getMarca());
            stmt.setString(4, painel.getModelo());
            stmt.setDouble(5, painel.getPotenciaW());
            stmt.setString(6, painel.getNotaFiscal());
            stmt.setString(7, painel.getStatus().name());
            stmt.setDate(8, Date.valueOf(painel.getDataInstalacao()));
            stmt.setInt(9, painel.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar painel: " + e.getMessage());
            return false;
        }
    }

    // DELETE

    public boolean excluir(int id) {
        String sql = "DELETE FROM painel WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir painel: " + e.getMessage());
            return false;
        }
    }
}