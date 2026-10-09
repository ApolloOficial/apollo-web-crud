package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.model.StringSolar;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StringSolarDAO extends ApolloConnection {

    private static final String COLUNAS = "id, id_inversor, codigo, numero_mppt, numero_entrada, status";

    public StringSolarDAO() {
        super();
    }

    private StringSolar montar(ResultSet rs) throws SQLException {
        StringSolar stringSolar = new StringSolar();
        stringSolar.setId(rs.getInt("id"));
        stringSolar.setIdInversor(rs.getInt("id_inversor"));
        stringSolar.setCodigo(rs.getString("codigo"));
        stringSolar.setNumeroMppt(rs.getObject("numero_mppt", Integer.class));
        stringSolar.setNumeroEntrada(rs.getObject("numero_entrada", Integer.class));
        stringSolar.setStatus(StatusGeral.valueOf(rs.getString("status")));
        return stringSolar;
    }

    // CREATE

    public boolean inserir(StringSolar stringSolar) {
        String sql = "INSERT INTO string (id_inversor, codigo, numero_mppt, numero_entrada, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, stringSolar.getIdInversor());
            stmt.setString(2, stringSolar.getCodigo());

            if (stringSolar.getNumeroMppt() != null) {
                stmt.setInt(3, stringSolar.getNumeroMppt());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            if (stringSolar.getNumeroEntrada() != null) {
                stmt.setInt(4, stringSolar.getNumeroEntrada());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setString(5, StatusGeral.ATIVO.name());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir string solar: " + e.getMessage());
            return false;
        }
    }

    // READ por id

    public StringSolar buscarPorId(int id) {
        String sql = "SELECT " + COLUNAS + " FROM string WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar string solar: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    public List<StringSolar> listarTodos() {
        List<StringSolar> lista = new ArrayList<>();
        String sql = "SELECT " + COLUNAS + " FROM string ORDER BY codigo";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar strings solares: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    public boolean atualizar(StringSolar stringSolar) {
        String sql = "UPDATE string SET id_inversor = ?, codigo = ?, numero_mppt = ?, numero_entrada = ?, status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, stringSolar.getIdInversor());
            stmt.setString(2, stringSolar.getCodigo());

            if (stringSolar.getNumeroMppt() != null) {
                stmt.setInt(3, stringSolar.getNumeroMppt());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            if (stringSolar.getNumeroEntrada() != null) {
                stmt.setInt(4, stringSolar.getNumeroEntrada());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setString(5, stringSolar.getStatus().name());
            stmt.setInt(6, stringSolar.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar string solar: " + e.getMessage());
            return false;
        }
    }

    // DELETE

    public boolean excluir(int id) {
        String sql = "DELETE FROM string WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir string solar: " + e.getMessage());
            return false;
        }
    }
}