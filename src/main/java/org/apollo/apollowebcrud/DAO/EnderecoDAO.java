package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Endereco;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acesso à tabela endereco, compartilhada por empresas e filiais. */
public class EnderecoDAO extends ApolloConnection {

    public EnderecoDAO() {
        super();
    }

    /** Transforma uma linha do resultado da consulta em um endereço. */
    private Endereco montar(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setId(rs.getInt("id"));
        endereco.setCep(rs.getString("cep"));
        endereco.setLogradouro(rs.getString("logradouro"));
        endereco.setNumero(rs.getString("numero"));
        endereco.setComplemento(rs.getString("complemento"));
        endereco.setBairro(rs.getString("bairro"));
        endereco.setCidade(rs.getString("cidade"));
        endereco.setEstado(rs.getString("estado"));
        return endereco;
    }

    // Usados pelo EmpresaDAO e pelo FilialDAO dentro da transação (recebem a conexão da transação)

    /** Insere o endereço dentro de uma transação já aberta e devolve o id gerado. */
    public int inserirNaConexao(Endereco endereco, Connection conn) throws SQLException {
        String sql = "INSERT INTO endereco (cep, logradouro, numero, complemento, bairro, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {
            stmt.setString(1, endereco.getCep());
            stmt.setString(2, endereco.getLogradouro());
            stmt.setString(3, endereco.getNumero());
            stmt.setString(4, endereco.getComplemento());
            stmt.setString(5, endereco.getBairro());
            stmt.setString(6, endereco.getCidade());
            stmt.setString(7, endereco.getEstado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    endereco.setId(id);
                    return id;
                }
            }
        }

        throw new SQLException("Não foi possível obter o id do endereço inserido.");
    }

    /** Atualiza o endereço dentro de uma transação já aberta. */
    public boolean atualizarNaConexao(Endereco endereco, Connection conn) throws SQLException {
        String sql = "UPDATE endereco SET cep = ?, logradouro = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, estado = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, endereco.getCep());
            stmt.setString(2, endereco.getLogradouro());
            stmt.setString(3, endereco.getNumero());
            stmt.setString(4, endereco.getComplemento());
            stmt.setString(5, endereco.getBairro());
            stmt.setString(6, endereco.getCidade());
            stmt.setString(7, endereco.getEstado());
            stmt.setInt(8, endereco.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    // CREATE

    /** Cadastra um endereço e devolve o id que o banco atribuiu. Em caso de falha o valor é -1. */
    public int inserir(Endereco endereco) {
        try (Connection connection = abrirConexao()) {
            return inserirNaConexao(endereco, connection);
        } catch (SQLException e) {
            System.err.println("Erro ao inserir endereço: " + e.getMessage());
            return -1;
        }
    }

    // READ por id

    /** Busca um endereço pelo id. Devolve null quando não encontra. */
    public Endereco buscarPorId(int id) {
        String sql = "SELECT * FROM endereco WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar endereço: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    /** Devolve todos os registros da tabela de endereço, ordenados no banco. */
    public List<Endereco> listarTodos() {
        List<Endereco> lista = new ArrayList<>();
        String sql = "SELECT * FROM endereco ORDER BY cidade, logradouro";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar endereços: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    /** Altera um endereço já existente e devolve true quando a atualização afetou um registro. */
    public boolean atualizar(Endereco endereco) {
        try (Connection connection = abrirConexao()) {
            return atualizarNaConexao(endereco, connection);
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar endereço: " + e.getMessage());
            return false;
        }
    }

    // DELETE

    /**
     * Remove um endereço do banco de forma definitiva. Para inativar sem apagar existe o InativacaoDAO.
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM endereco WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir endereço: " + e.getMessage());
            return false;
        }
    }
}