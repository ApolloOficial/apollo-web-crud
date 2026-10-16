package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.Segmento;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SegmentoDAO extends ApolloConnection {

    public SegmentoDAO() {
        super();
    }

    private Segmento montar(ResultSet rs) throws SQLException {
        Segmento segmento = new Segmento();
        segmento.setId(rs.getInt("id"));
        segmento.setNome(rs.getString("nome"));
        segmento.setDescricao(rs.getString("descricao"));
        return segmento;
    }

// CREATE

    public int inserir(Segmento segmento) {
        String sql = "INSERT INTO segmento (nome, descricao) VALUES (?, ?)";

        // retorna o id gerado pelo banco
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, segmento.getNome());
            stmt.setString(2, segmento.getDescricao());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return -1;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir segmento: " + e.getMessage());
            return -1;
        }
    }

    // READ por id

    public Segmento buscarPorId(int id) {
        String sql = "SELECT * FROM segmento WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar segmento: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    public List<Segmento> listarTodos() {
        List<Segmento> lista = new ArrayList<>();
        String sql = "SELECT * FROM segmento ORDER BY nome";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar segmentos: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    public boolean atualizar(Segmento segmento) {
        String sql = "UPDATE segmento SET nome = ?, descricao = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, segmento.getNome());
            stmt.setString(2, segmento.getDescricao());
            stmt.setInt(3, segmento.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar segmento: " + e.getMessage());
            return false;
        }
    }

    // DELETE

    public boolean excluir(int id) {
        String sql = "DELETE FROM segmento WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir segmento: " + e.getMessage());
            return false;
        }
    }
}