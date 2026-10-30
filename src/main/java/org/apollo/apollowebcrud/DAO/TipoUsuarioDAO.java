package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.TipoUsuario;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoUsuarioDAO extends ApolloConnection {

    public TipoUsuarioDAO() {
        super();
    }

    private TipoUsuario montar(ResultSet rs) throws SQLException {
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(rs.getInt("id"));
        tipoUsuario.setNome(TipoUsuEnum.valueOf(rs.getString("nome")));
        tipoUsuario.setDescricao(rs.getString("descricao"));
        return tipoUsuario;
    }

// CREATE

    public int inserir(TipoUsuario tipoUsuario) {
        String sql = "INSERT INTO tipo_usuario (nome, descricao) VALUES (?, ?)";

        // retorna o id gerado pelo banco
        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tipoUsuario.getNome().name());
            stmt.setString(2, tipoUsuario.getDescricao());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return -1;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir tipo de usuário: " + e.getMessage());
            return -1;
        }
    }

    // READ por id

    public TipoUsuario buscarPorId(int id) {
        String sql = "SELECT * FROM tipo_usuario WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
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

    public TipoUsuario buscarPorNome(String nome) {
        String sql = "SELECT * FROM tipo_usuario WHERE nome = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);

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
        String sql = "SELECT * FROM tipo_usuario ORDER BY nome";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql);
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

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
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

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir tipo de usuário: " + e.getMessage());
            return false;
        }
    }
}