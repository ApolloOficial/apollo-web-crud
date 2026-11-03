package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Inversor;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso à tabela inversor. As consultas por filial garantem que cada equipe veja apenas os seus equipamentos.
 */
public class InversorDAO extends ApolloConnection {

    public InversorDAO() {
        super();
    }

    /** Transforma uma linha do resultado da consulta em um inversor. */
    private Inversor montar(ResultSet rs) throws SQLException {
        Inversor inversor = new Inversor();
        inversor.setId(rs.getInt("id"));
        inversor.setIdFilial(rs.getInt("id_filial"));
        inversor.setCodigo(rs.getString("codigo"));
        inversor.setNumeroSerie(rs.getString("numero_serie"));
        inversor.setMarca(rs.getString("marca"));
        inversor.setModelo(rs.getString("modelo"));
        double potenciaKw = rs.getDouble("potencia_kw");
        inversor.setPotenciaKw(rs.wasNull() ? null : potenciaKw);
        inversor.setStatus(StatusGeral.valueOf(rs.getString("status")));
        Date dataInstalacao = rs.getDate("data_instalacao");
        inversor.setDataInstalacao(dataInstalacao != null ? dataInstalacao.toLocalDate() : null);
        return inversor;
    }

// CREATE

    /** Cadastra um inversor e devolve o id que o banco atribuiu. Em caso de falha o valor é -1. */
    public int inserir(Inversor inversor) {
        String sql = "INSERT INTO inversor (id_filial, codigo, numero_serie, marca, modelo, potencia_kw, status, data_instalacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // retorna o id gerado pelo banco
        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, inversor.getIdFilial());
            stmt.setString(2, inversor.getCodigo());
            stmt.setString(3, inversor.getNumeroSerie());
            stmt.setString(4, inversor.getMarca());
            stmt.setString(5, inversor.getModelo());
            stmt.setObject(6, inversor.getPotenciaKw(), Types.NUMERIC);
            stmt.setString(7, StatusGeral.ATIVO.name());
            stmt.setObject(8, inversor.getDataInstalacao() == null ? null : Date.valueOf(inversor.getDataInstalacao()), Types.DATE);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return -1;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir inversor: " + e.getMessage());
            return -1;
        }
    }

    // READ por id

    /** Busca um inversor pelo id. Devolve null quando não encontra. */
    public Inversor buscarPorId(int id) {
        String sql = "SELECT * FROM inversor WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar inversor: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    /** Devolve todos os registros da tabela de inversor, ordenados no banco. */
    public List<Inversor> listarTodos() {
        List<Inversor> lista = new ArrayList<>();
        String sql = "SELECT * FROM inversor ORDER BY codigo";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar inversores: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    /** Altera um inversor já existente e devolve true quando a atualização afetou um registro. */
    public boolean atualizar(Inversor inversor) {
        String sql = "UPDATE inversor SET id_filial = ?, codigo = ?, numero_serie = ?, marca = ?, modelo = ?, potencia_kw = ?, status = ?, data_instalacao = ? WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, inversor.getIdFilial());
            stmt.setString(2, inversor.getCodigo());
            stmt.setString(3, inversor.getNumeroSerie());
            stmt.setString(4, inversor.getMarca());
            stmt.setString(5, inversor.getModelo());
            stmt.setObject(6, inversor.getPotenciaKw(), Types.NUMERIC);
            stmt.setString(7, inversor.getStatus().name());
            stmt.setObject(8, inversor.getDataInstalacao() == null ? null : Date.valueOf(inversor.getDataInstalacao()), Types.DATE);
            stmt.setInt(9, inversor.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar inversor: " + e.getMessage());
            return false;
        }
    }

    // DELETE

    /**
     * Remove um inversor do banco de forma definitiva. Para inativar sem apagar existe o InativacaoDAO.
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM inversor WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir inversor: " + e.getMessage());
            return false;
        }
    }

    /** Lista os inversores de uma filial. */
    public List<Inversor> listarPorFilial(int idFilial) {
        List<Inversor> lista = new ArrayList<>();
        String sql = "SELECT * FROM inversor WHERE id_filial = ? ORDER BY codigo";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFilial);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar inversores da filial: " + e.getMessage());
        }

        return lista;
    }

    /** Procura pelo número de série, que é único em todo o sistema. */
    public Inversor buscarPorNumeroSerie(String numeroSerie) {
        return buscarUm("SELECT * FROM inversor WHERE numero_serie = ?", numeroSerie, null);
    }

    /**
     * Procura pelo código dentro de uma filial, já que o mesmo código pode existir em filiais diferentes.
     */
    public Inversor buscarPorCodigoNaFilial(int idFilial, String codigo) {
        return buscarUm("SELECT * FROM inversor WHERE id_filial = ? AND codigo = ?", codigo, idFilial);
    }

    /**
     * Executa a consulta de um único inversor. O idFilial pode ser null quando a busca usa só o texto.
     */
    private Inversor buscarUm(String sql, String texto, Integer idFilial) {
        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            int posicao = 1;
            if (idFilial != null) {
                stmt.setInt(posicao++, idFilial);
            }
            stmt.setString(posicao, texto);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar inversor: " + e.getMessage());
        }

        return null;
    }
}
