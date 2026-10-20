package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends ApolloConnection {

    public UsuarioDAO() {
        super();
    }

    private Usuario montar(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
        usuario.setIdFilial(rs.getInt("id_filial"));
        usuario.setEmail(rs.getString("email"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setCpf(rs.getString("cpf"));
        usuario.setStatus(rs.getString("ativo"));
        return usuario;
    }

    // CREATE

    public int inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, id_tipo_usuario, id_filial, email, telefone, cpf, senha_hash, ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        // retorna o id gerado pelo banco
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setInt(2, usuario.getIdTipoUsuario());
            stmt.setInt(3, usuario.getIdFilial());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getTelefone());
            stmt.setString(6, usuario.getCpf());
            stmt.setString(7, usuario.getSenhaHash());
            stmt.setString(8, "ATIVO");

            return stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
            return -1;
        }
    }

    // READ por id

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }

    public Usuario buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM usuario WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY nome";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, id_tipo_usuario = ?, id_filial = ?, email = ?, telefone = ?, cpf = ?, ativo = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setInt(2, usuario.getIdTipoUsuario());
            stmt.setInt(3, usuario.getIdFilial());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getTelefone());
            stmt.setString(6, usuario.getCpf());
            stmt.setString(7, usuario.getStatus());
            stmt.setInt(8, usuario.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }

    // DELETE

    public boolean excluir(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir usuário: " + e.getMessage());
            return false;
        }
    }

    // VALIDADOR

    public Usuario validarLogin (String email, String cpf, String senha) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND cpf = ? AND senha_hash = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, cpf);
            stmt.setString(3, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }
}