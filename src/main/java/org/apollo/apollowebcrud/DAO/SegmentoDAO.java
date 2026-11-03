package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Segmento;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acesso à tabela segmento, com os ramos de atuação que a filial pode escolher no cadastro. */
public class SegmentoDAO extends ApolloConnection {

    public SegmentoDAO() {
        super();
    }

    /** Transforma uma linha do resultado da consulta em um segmento. */
    private Segmento montar(ResultSet rs) throws SQLException {
        Segmento segmento = new Segmento();
        segmento.setId(rs.getInt("id"));
        segmento.setNome(rs.getString("nome"));
        segmento.setDescricao(rs.getString("descricao"));
        return segmento;
    }

// CREATE

    /** Cadastra um segmento e devolve o id que o banco atribuiu. Em caso de falha o valor é -1. */
    public int inserir(Segmento segmento) {
        String sql = "INSERT INTO segmento (nome, descricao) VALUES (?, ?)";

        // retorna o id gerado pelo banco
        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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

    /** Busca um segmento pelo id. Devolve null quando não encontra. */
    public Segmento buscarPorId(int id) {
        String sql = "SELECT * FROM segmento WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
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

    /** Devolve todos os registros da tabela de segmento, ordenados no banco. */
    public List<Segmento> listarTodos() {
        List<Segmento> lista = new ArrayList<>();
        String sql = "SELECT * FROM segmento ORDER BY nome";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql);
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

    /** Altera um segmento já existente e devolve true quando a atualização afetou um registro. */
    public boolean atualizar(Segmento segmento) {
        String sql = "UPDATE segmento SET nome = ?, descricao = ? WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
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

    /**
     * Remove um segmento do banco de forma definitiva. Para inativar sem apagar existe o InativacaoDAO.
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM segmento WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir segmento: " + e.getMessage());
            return false;
        }
    }
}