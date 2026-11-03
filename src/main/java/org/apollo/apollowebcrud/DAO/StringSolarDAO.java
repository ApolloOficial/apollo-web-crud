package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.model.StringSolar;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acesso à tabela string, que agrupa painéis ligados a uma mesma entrada de um inversor. */
public class StringSolarDAO extends ApolloConnection {

    public StringSolarDAO() {
        super();
    }

    /** Transforma uma linha do resultado da consulta em uma string. */
    private StringSolar montar(ResultSet rs) throws SQLException {
        StringSolar stringSolar = new StringSolar();
        stringSolar.setId(rs.getInt("id"));
        stringSolar.setIdInversor(rs.getInt("id_inversor"));
        stringSolar.setCodigo(rs.getString("codigo"));
        stringSolar.setNumeroMppt(rs.getObject("numero_mppt", Integer.class));
        stringSolar.setNumeroEntrada(rs.getObject("numero_entrada", Integer.class));
        stringSolar.setStatus(StatusGeral.valueOf(rs.getString("status")));
        return stringSolar;
    }

// CREATE

    /** Cadastra uma string e devolve o id que o banco atribuiu. Em caso de falha o valor é -1. */
    public int inserir(StringSolar stringSolar) {
        String sql = "INSERT INTO string (id_inversor, codigo, numero_mppt, numero_entrada, status) VALUES (?, ?, ?, ?, ?)";

        // retorna o id gerado pelo banco
        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, stringSolar.getIdInversor());
            stmt.setString(2, stringSolar.getCodigo());

            if (stringSolar.getNumeroMppt() != null) {
                stmt.setInt(3, stringSolar.getNumeroMppt());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            if (stringSolar.getNumeroEntrada() != null) {
                stmt.setInt(4, stringSolar.getNumeroEntrada());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setString(5, StatusGeral.ATIVO.name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return -1;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir string solar: " + e.getMessage());
            return -1;
        }
    }

    // READ por id

    /** Busca uma string pelo id. Devolve null quando não encontra. */
    public StringSolar buscarPorId(int id) {
        String sql = "SELECT * FROM string WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar string solar: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    /** Devolve todos os registros da tabela de string, ordenados no banco. */
    public List<StringSolar> listarTodos() {
        List<StringSolar> lista = new ArrayList<>();
        String sql = "SELECT * FROM string ORDER BY codigo";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar strings solares: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    /** Altera uma string já existente e devolve true quando a atualização afetou um registro. */
    public boolean atualizar(StringSolar stringSolar) {
        String sql = "UPDATE string SET id_inversor = ?, codigo = ?, numero_mppt = ?, numero_entrada = ?, status = ? WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, stringSolar.getIdInversor());
            stmt.setString(2, stringSolar.getCodigo());

            if (stringSolar.getNumeroMppt() != null) {
                stmt.setInt(3, stringSolar.getNumeroMppt());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            if (stringSolar.getNumeroEntrada() != null) {
                stmt.setInt(4, stringSolar.getNumeroEntrada());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setString(5, stringSolar.getStatus().name());
            stmt.setInt(6, stringSolar.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar string solar: " + e.getMessage());
            return false;
        }
    }

    // DELETE

    /**
     * Remove uma string do banco de forma definitiva. Para inativar sem apagar existe o InativacaoDAO.
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM string WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir string solar: " + e.getMessage());
            return false;
        }
    }

    /** Lista as strings da filial, passando pelo inversor de cada uma. */
    public List<StringSolar> listarPorFilial(int idFilial) {
        List<StringSolar> lista = new ArrayList<>();
        String sql = "SELECT s.* FROM string s JOIN inversor i ON i.id = s.id_inversor WHERE i.id_filial = ? ORDER BY s.codigo";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFilial);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar strings da filial: " + e.getMessage());
        }

        return lista;
    }

    /** Lista as strings de um inversor. */
    public List<StringSolar> listarPorInversor(int idInversor) {
        List<StringSolar> lista = new ArrayList<>();
        String sql = "SELECT * FROM string WHERE id_inversor = ? ORDER BY codigo";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idInversor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar strings do inversor: " + e.getMessage());
        }

        return lista;
    }

    /** Procura pelo código dentro de um inversor, onde ele não pode se repetir. */
    public StringSolar buscarPorCodigoNoInversor(int idInversor, String codigo) {
        String sql = "SELECT * FROM string WHERE id_inversor = ? AND codigo = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idInversor);
            stmt.setString(2, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar string por código: " + e.getMessage());
        }

        return null;
    }
}
