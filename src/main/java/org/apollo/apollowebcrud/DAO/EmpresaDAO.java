package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.Empresa;
import org.apollo.apollowebcrud.model.Endereco;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO extends ApolloConnection {

    private static final String COLUNAS = "id, nome, descricao, cnpj, email_contato, id_endereco, ativo, url_foto, id_usuario_cadastro";

    public EmpresaDAO() {
        super();
    }

    private Empresa montar(ResultSet rs) throws SQLException {
        Empresa empresa = new Empresa();
        empresa.setId(rs.getInt("id"));
        empresa.setNome(rs.getString("nome"));
        empresa.setDescricao(rs.getString("descricao"));
        empresa.setCnpj(rs.getString("cnpj"));
        empresa.setEmailContato(rs.getString("email_contato"));
        empresa.setIdEndereco(rs.getInt("id_endereco"));
        empresa.setAtivo(StatusGeral.valueOf(rs.getString("ativo")));
        empresa.setUrlFoto(rs.getString("url_foto"));
        empresa.setIdUsuarioCadastro(rs.getObject("id_usuario_cadastro", Integer.class));
        return empresa;
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

    public boolean inserir(Empresa empresa, Endereco endereco) {
        String sql = "INSERT INTO empresa (nome, descricao, cnpj, email_contato, id_endereco, ativo, url_foto, id_usuario_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            int idEndereco = EnderecoDAO.inserirNaConexao(endereco, connection);
            empresa.setIdEndereco(idEndereco);

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, empresa.getNome());
                stmt.setString(2, empresa.getDescricao());
                stmt.setString(3, empresa.getCnpj());
                stmt.setString(4, empresa.getEmailContato());
                stmt.setInt(5, empresa.getIdEndereco());
                stmt.setString(6, StatusGeral.ATIVO.name());
                stmt.setString(7, empresa.getUrlFoto());

                if (empresa.getIdUsuarioCadastro() != null) {
                    stmt.setInt(8, empresa.getIdUsuarioCadastro());
                } else {
                    stmt.setNull(8, Types.INTEGER);
                }

                stmt.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            desfazer();
            System.err.println("Erro ao inserir empresa: " + e.getMessage());
            return false;

        } finally {
            restaurarAutoCommit();
        }
    }

    // READ por id

    public Empresa buscarPorId(int id) {
        String sql = "SELECT " + COLUNAS + " FROM empresa WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar empresa: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    public List<Empresa> listarTodos() {
        List<Empresa> lista = new ArrayList<>();
        String sql = "SELECT " + COLUNAS + " FROM empresa ORDER BY nome";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar empresas: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    public boolean atualizar(Empresa empresa, Endereco endereco) {
        String sql = "UPDATE empresa SET nome = ?, descricao = ?, cnpj = ?, email_contato = ?, ativo = ?, url_foto = ? WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            endereco.setId(empresa.getIdEndereco());
            EnderecoDAO.atualizarNaConexao(endereco, connection);

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, empresa.getNome());
                stmt.setString(2, empresa.getDescricao());
                stmt.setString(3, empresa.getCnpj());
                stmt.setString(4, empresa.getEmailContato());
                stmt.setString(5, empresa.getAtivo().name());
                stmt.setString(6, empresa.getUrlFoto());
                stmt.setInt(7, empresa.getId());

                boolean atualizou = stmt.executeUpdate() > 0;
                connection.commit();
                return atualizou;
            }

        } catch (SQLException e) {
            desfazer();
            System.err.println("Erro ao atualizar empresa: " + e.getMessage());
            return false;

        } finally {
            restaurarAutoCommit();
        }
    }

    // DELETE

    public boolean excluir(int id) {
        String sql = "DELETE FROM empresa WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir empresa: " + e.getMessage());
            return false;
        }
    }
}