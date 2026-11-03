package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Contrato;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso à tabela contrato. Cada empresa possui um contrato, que nasce junto com ela
 * e depois só é consultado pelo gerente.
 */
public class ContratoDAO extends ApolloConnection {

    public ContratoDAO() {
        super();
    }

    /** Transforma uma linha do resultado da consulta em um contrato. */
    private Contrato montar(ResultSet rs) throws SQLException {
        Contrato contrato = new Contrato();
        contrato.setId(rs.getInt("id"));
        contrato.setIdEmpresa(rs.getInt("id_empresa"));
        contrato.setDataInicio(rs.getDate("data_inicio").toLocalDate());
        contrato.setValorMensal(rs.getBigDecimal("valor_mensal"));
        contrato.setStatus(StatusGeral.valueOf(rs.getString("status")));
        contrato.setIdUsuarioCadastro(rs.getObject("id_usuario_cadastro", Integer.class));
        return contrato;
    }

    // CREATE

    /** Cadastra um contrato e devolve o id que o banco atribuiu. Em caso de falha o valor é -1. */
    public int inserir(Contrato contrato) {
        try (Connection connection = abrirConexao()) {
            return inserirNaConexao(connection, contrato);

        } catch (SQLException e) {
            System.err.println("Erro ao inserir contrato: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Insere o contrato usando uma conexão já aberta. Serve para transações em que quem chama é responsável por fechar a conexão.
     */
    public int inserirNaConexao(Connection connection, Contrato contrato) throws SQLException {
        String sql = "INSERT INTO contrato (id_empresa, data_inicio, valor_mensal, status, id_usuario_cadastro) VALUES (?, ?, ?, ?, ?)";

        // retorna o id gerado pelo banco
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, contrato.getIdEmpresa());
            stmt.setDate(2, Date.valueOf(contrato.getDataInicio()));
            stmt.setBigDecimal(3, contrato.getValorMensal());
            stmt.setString(4, StatusGeral.ATIVO.name());
            stmt.setObject(5, contrato.getIdUsuarioCadastro(), Types.INTEGER);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("Não foi possível obter o id do contrato inserido.");
    }

    // READ por id

    /** Busca um contrato pelo id. Devolve null quando não encontra. */
    public Contrato buscarPorId(int id) {
        String sql = "SELECT * FROM contrato WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar contrato: " + e.getMessage());
        }

        return null;
    }

    /** Busca o contrato de uma empresa, ou null se ela ainda não tiver um. */
    public Contrato buscarPorEmpresa(int idEmpresa) {
        String sql = "SELECT * FROM contrato WHERE id_empresa = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEmpresa);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar contrato da empresa: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    /** Devolve todos os registros da tabela de contrato, ordenados no banco. */
    public List<Contrato> listarTodos() {
        List<Contrato> lista = new ArrayList<>();
        String sql = "SELECT * FROM contrato ORDER BY data_inicio DESC";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar contratos: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    /** Altera um contrato já existente e devolve true quando a atualização afetou um registro. */
    public boolean atualizar(Contrato contrato) {
        String sql = "UPDATE contrato SET id_empresa = ?, data_inicio = ?, valor_mensal = ?, status = ? WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, contrato.getIdEmpresa());
            stmt.setDate(2, Date.valueOf(contrato.getDataInicio()));
            stmt.setBigDecimal(3, contrato.getValorMensal());
            stmt.setString(4, contrato.getStatus().name());
            stmt.setInt(5, contrato.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar contrato: " + e.getMessage());
            return false;
        }
    }

    // DELETE

    /**
     * Remove um contrato do banco de forma definitiva. Para inativar sem apagar existe o InativacaoDAO.
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM contrato WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir contrato: " + e.getMessage());
            return false;
        }
    }
}
