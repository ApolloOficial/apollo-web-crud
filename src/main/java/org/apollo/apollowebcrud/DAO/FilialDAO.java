package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.Endereco;
import org.apollo.apollowebcrud.model.Filial;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilialDAO extends ApolloConnection {

    private static final String COLUNAS = "id, id_empresa, id_segmento, nome, cnpj, email, telefone, id_endereco, ativo, id_usuario_cadastro";

    public FilialDAO() {
        super();
    }

    private Filial montar(ResultSet rs) throws SQLException {
        Filial filial = new Filial();
        filial.setId(rs.getInt("id"));
        filial.setIdEmpresa(rs.getInt("id_empresa"));
        filial.setIdSegmento(rs.getInt("id_segmento"));
        filial.setNome(rs.getString("nome"));
        filial.setCnpj(rs.getString("cnpj"));
        filial.setEmail(rs.getString("email"));
        filial.setTelefone(rs.getString("telefone"));
        filial.setIdEndereco(rs.getInt("id_endereco"));
        filial.setAtivo(StatusGeral.valueOf(rs.getString("ativo")));
        filial.setIdUsuarioCadastro(rs.getObject("id_usuario_cadastro", Integer.class));
        return filial;
    }

    private void desfazer() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.err.println("Erro ao desfazer transação: " + e.getMessage());
        }
    }

    private void restaurarAutoCommit() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Erro ao restaurar auto-commit: " + e.getMessage());
        }
    }

    // CREATE

    public boolean inserir(Filial filial, Endereco endereco) {
        String sql = "INSERT INTO filial (id_empresa, id_segmento, nome, cnpj, email, telefone, id_endereco, ativo, id_usuario_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            int idEndereco = EnderecoDAO.inserirNaConexao(endereco, connection);
            filial.setIdEndereco(idEndereco);

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, filial.getIdEmpresa());
                stmt.setInt(2, filial.getIdSegmento());
                stmt.setString(3, filial.getNome());
                stmt.setString(4, filial.getCnpj());
                stmt.setString(5, filial.getEmail());
                stmt.setString(6, filial.getTelefone());
                stmt.setInt(7, filial.getIdEndereco());
                stmt.setString(8, StatusGeral.ATIVO.name());

                if (filial.getIdUsuarioCadastro() != null) {
                    stmt.setInt(9, filial.getIdUsuarioCadastro());
                } else {
                    stmt.setNull(9, Types.INTEGER);
                }

                stmt.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            desfazer();
            System.err.println("Erro ao inserir filial: " + e.getMessage());
            return false;

        } finally {
            restaurarAutoCommit();
        }
    }

    // READ por id

    public Filial buscarPorId(int id) {
        String sql = "SELECT " + COLUNAS + " FROM filial WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar filial: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    public List<Filial> listarTodos() {
        List<Filial> lista = new ArrayList<>();
        String sql = "SELECT " + COLUNAS + " FROM filial ORDER BY nome";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar filiais: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    public boolean atualizar(Filial filial, Endereco endereco) {
        String sql = "UPDATE filial SET id_empresa = ?, id_segmento = ?, nome = ?, cnpj = ?, email = ?, telefone = ?, ativo = ? WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            endereco.setId(filial.getIdEndereco());
            EnderecoDAO.atualizarNaConexao(endereco, connection);

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, filial.getIdEmpresa());
                stmt.setInt(2, filial.getIdSegmento());
                stmt.setString(3, filial.getNome());
                stmt.setString(4, filial.getCnpj());
                stmt.setString(5, filial.getEmail());
                stmt.setString(6, filial.getTelefone());
                stmt.setString(7, filial.getAtivo().name());
                stmt.setInt(8, filial.getId());

                boolean atualizou = stmt.executeUpdate() > 0;
                connection.commit();
                return atualizou;
            }

        } catch (SQLException e) {
            desfazer();
            System.err.println("Erro ao atualizar filial: " + e.getMessage());
            return false;

        } finally {
            restaurarAutoCommit();
        }
    }

    // DELETE

    public boolean excluir(int id) {
        String sql = "DELETE FROM filial WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir filial: " + e.getMessage());
            return false;
        }
    }
}