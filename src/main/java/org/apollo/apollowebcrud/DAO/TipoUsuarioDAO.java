package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.TipoUsuario;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoUsuarioDAO extends ApolloConnection {

    private static final String COLUNAS = "id, nome, descricao";

    public TipoUsuarioDAO() {
        super();
    }

    private TipoUsuario montar(ResultSet rs) throws SQLException {
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(rs.getInt("id"));
        tipoUsuario.setNome(TipoUsuario.Tipo.valueOf(rs.getString("nome")));
        tipoUsuario.setDescricao(rs.getString("descricao"));
        return tipoUsuario;
    }

    // CREATE

    public boolean inserir(TipoUsuario tipoUsuario) {
        String sql = "INSERT INTO tipo_usuario (nome, descricao) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoUsuario.getNome().name());
            stmt.setString(2, tipoUsuario.getDescricao());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir tipo de usuário: " + e.getMessage());
            return false;
        }
    }

    // READ por id

    public TipoUsuario buscarPorId(int id) {
        String sql = "SELECT " + COLUNAS + " FROM tipo_usuario WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar tipo de usuário: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    public List<TipoUsuario> listarTodos() {
        List<TipoUsuario> lista = new ArrayList<>();
        String sql = "SELECT " + COLUNAS + " FROM tipo_usuario ORDER BY nome";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar tipos de usuário: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    public boolean atualizar(TipoUsuario tipoUsuario) {
        String sql = "UPDATE tipo_usuario SET nome = ?, descricao = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoUsuario.getNome().name());
            stmt.setString(2, tipoUsuario.getDescricao());
            stmt.setInt(3, tipoUsuario.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar tipo de usuário: " + e.getMessage());
            return false;
        }
    }

    // DELETE

    public boolean excluir(int id) {
        String sql = "DELETE FROM tipo_usuario WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir tipo de usuário: " + e.getMessage());
            return false;
        }
    }
}