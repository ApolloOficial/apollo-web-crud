package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Painel;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso à tabela painel. Cada painel pertence a uma string e, por meio dela, a um inversor e a uma filial.
 */
public class PainelDAO extends ApolloConnection {

    public PainelDAO() {
        super();
    }

    /** Lê as colunas da linha corrente e monta um painel. */
    private Painel montar(ResultSet rs) throws SQLException {
        Painel painel = new Painel();
        painel.setId(rs.getInt("id"));
        painel.setIdString(rs.getInt("id_string"));
        painel.setNumeroSerie(rs.getString("numero_serie"));
        painel.setMarca(rs.getString("marca"));
        painel.setModelo(rs.getString("modelo"));
        double potenciaW = rs.getDouble("potencia_w");
        painel.setPotenciaW(rs.wasNull() ? null : potenciaW);
        painel.setNotaFiscal(rs.getString("nota_fiscal"));
        painel.setStatus(StatusGeral.valueOf(rs.getString("status")));
        Date dataInstalacao = rs.getDate("data_instalacao");
        painel.setDataInstalacao(dataInstalacao != null ? dataInstalacao.toLocalDate() : null);
        return painel;
    }

// CREATE

    /** Grava um painel e devolve o id gerado pelo banco, ou -1 se a gravação falhar. */
    public int inserir(Painel painel) {
        String sql = "INSERT INTO painel (id_string, numero_serie, marca, modelo, potencia_w, nota_fiscal, status, data_instalacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // retorna o id gerado pelo banco
        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, painel.getIdString());
            stmt.setString(2, painel.getNumeroSerie());
            stmt.setString(3, painel.getMarca());
            stmt.setString(4, painel.getModelo());
            stmt.setObject(5, painel.getPotenciaW(), Types.NUMERIC);
            stmt.setString(6, painel.getNotaFiscal());
            stmt.setString(7, StatusGeral.ATIVO.name());
            stmt.setObject(8, painel.getDataInstalacao() == null ? null : Date.valueOf(painel.getDataInstalacao()), Types.DATE);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return -1;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir painel: " + e.getMessage());
            return -1;
        }
    }

    // READ por id

    /** Procura um painel com o id informado, retornando null se ele não existir. */
    public Painel buscarPorId(int id) {
        String sql = "SELECT * FROM painel WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar painel: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    /**
     * Retorna a lista completa de painel. A lista fica vazia se não houver registros ou se ocorrer erro.
     */
    public List<Painel> listarTodos() {
        List<Painel> lista = new ArrayList<>();
        String sql = "SELECT * FROM painel ORDER BY marca, modelo";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar painéis: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    /** Atualiza os dados de um painel identificado pelo id. Devolve true se alguma linha foi alterada. */
    public boolean atualizar(Painel painel) {
        String sql = "UPDATE painel SET id_string = ?, numero_serie = ?, marca = ?, modelo = ?, potencia_w = ?, nota_fiscal = ?, status = ?, data_instalacao = ? WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, painel.getIdString());
            stmt.setString(2, painel.getNumeroSerie());
            stmt.setString(3, painel.getMarca());
            stmt.setString(4, painel.getModelo());
            stmt.setObject(5, painel.getPotenciaW(), Types.NUMERIC);
            stmt.setString(6, painel.getNotaFiscal());
            stmt.setString(7, painel.getStatus().name());
            stmt.setObject(8, painel.getDataInstalacao() == null ? null : Date.valueOf(painel.getDataInstalacao()), Types.DATE);
            stmt.setInt(9, painel.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar painel: " + e.getMessage());
            return false;
        }
    }

    // DELETE

    /** Exclui um painel pelo id e devolve true se algo foi apagado. */
    public boolean excluir(int id) {
        String sql = "DELETE FROM painel WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir painel: " + e.getMessage());
            return false;
        }
    }

    /** Lista os painéis da filial, seguindo o caminho string e inversor. */
    public List<Painel> listarPorFilial(int idFilial) {
        List<Painel> lista = new ArrayList<>();
        String sql = "SELECT p.* FROM painel p JOIN string s ON s.id = p.id_string JOIN inversor i ON i.id = s.id_inversor WHERE i.id_filial = ? ORDER BY p.numero_serie";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFilial);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar painéis da filial: " + e.getMessage());
        }

        return lista;
    }

    /** Lista os painéis ligados a uma string. */
    public List<Painel> listarPorString(int idString) {
        List<Painel> lista = new ArrayList<>();
        String sql = "SELECT * FROM painel WHERE id_string = ? ORDER BY numero_serie";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idString);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar painéis da string: " + e.getMessage());
        }

        return lista;
    }

    /** Procura pelo número de série, usado para evitar painéis duplicados. */
    public Painel buscarPorNumeroSerie(String numeroSerie) {
        String sql = "SELECT * FROM painel WHERE numero_serie = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroSerie);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar painel por número de série: " + e.getMessage());
        }

        return null;
    }
}
