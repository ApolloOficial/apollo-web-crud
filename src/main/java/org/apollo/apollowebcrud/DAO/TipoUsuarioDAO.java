package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.TipoUsuario;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acesso à tabela tipo_usuario, que relaciona cada id do banco com o perfil usado pela aplicação. */
public class TipoUsuarioDAO extends ApolloConnection {

    public TipoUsuarioDAO() {
        super();
    }

    /** Transforma uma linha do resultado da consulta em um tipo de usuário. */
    private TipoUsuario montar(ResultSet rs) throws SQLException {
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(rs.getInt("id"));
        tipoUsuario.setNome(TipoUsuEnum.valueOf(rs.getString("nome")));
        tipoUsuario.setDescricao(rs.getString("descricao"));
        return tipoUsuario;
    }

// CREATE

    /** Cadastra um tipo de usuário e devolve o id que o banco atribuiu. Em caso de falha o valor é -1. */
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

    /** Busca um tipo de usuário pelo id. Devolve null quando não encontra. */
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

    /** Procura o tipo pelo nome do perfil, como GERENTE ou TECNICO. */
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

    /** Devolve todos os registros da tabela de tipo de usuário, ordenados no banco. */
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

    /** Altera um tipo de usuário já existente e devolve true quando a atualização afetou um registro. */
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

    /**
     * Remove um tipo de usuário do banco de forma definitiva. Para inativar sem apagar existe o InativacaoDAO.
     */
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