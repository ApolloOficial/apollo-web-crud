package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Contrato;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContratoDAO extends ApolloConnection {

    public ContratoDAO() {
        super();
    }

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

    public int inserir(Contrato contrato) {
        try (Connection connection = abrirConexao()) {
            return inserirNaConexao(connection, contrato);

        } catch (SQLException e) {
            System.err.println("Erro ao inserir contrato: " + e.getMessage());
            return -1;
        }
    }

    // Usado dentro de transações (a conexão é da transação, quem chama é que fecha)
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
