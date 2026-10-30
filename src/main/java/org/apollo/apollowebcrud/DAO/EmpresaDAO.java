package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Empresa;
import org.apollo.apollowebcrud.model.Endereco;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO extends ApolloConnection {

    private static final String COLUNAS = "id, nome, descricao, cnpj, email_contato, id_endereco, ativo, url_foto, id_usuario_cadastro";

    private final EnderecoDAO enderecoDAO = new EnderecoDAO();

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
        empresa.setStatus(StatusGeral.valueOf(rs.getString("ativo")));
        empresa.setUrlFoto(rs.getString("url_foto"));
        empresa.setIdUsuarioCadastro(rs.getObject("id_usuario_cadastro", Integer.class));
        return empresa;
    }

    // CREATE

    public int inserir(Empresa empresa, Endereco endereco) {
        try (Connection connection = abrirConexao()) {
            connection.setAutoCommit(false);

            try {
                int idGerado = inserirNaConexao(connection, empresa, endereco);
                connection.commit();
                return idGerado;

            } catch (SQLException e) {
                desfazer(connection);
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir empresa: " + e.getMessage());
            return -1;
        }
    }

    // grava endereço + empresa na mesma transação; devolve o id gerado
    public int inserirNaConexao(Connection connection, Empresa empresa, Endereco endereco) throws SQLException {
        String sql = "INSERT INTO empresa (nome, descricao, cnpj, email_contato, id_endereco, ativo, url_foto, id_usuario_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int idEndereco = enderecoDAO.inserirNaConexao(endereco, connection);
        empresa.setIdEndereco(idEndereco);

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, empresa.getNome());
            stmt.setString(2, empresa.getDescricao());
            stmt.setString(3, empresa.getCnpj());
            stmt.setString(4, empresa.getEmailContato());
            stmt.setInt(5, empresa.getIdEndereco());
            stmt.setString(6, StatusGeral.ATIVO.name());
            stmt.setString(7, empresa.getUrlFoto());
            stmt.setObject(8, empresa.getIdUsuarioCadastro(), Types.INTEGER);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("Não foi possível obter o id gerado.");
    }

    // READ por id

    public Empresa buscarPorId(int id) {
        String sql = "SELECT " + COLUNAS + " FROM empresa WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
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

    public Empresa buscarPorCnpj(String cnpj) {
        String sql = "SELECT " + COLUNAS + " FROM empresa WHERE cnpj = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cnpj);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar empresa por CNPJ: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    public List<Empresa> listarTodos() {
        List<Empresa> lista = new ArrayList<>();
        String sql = "SELECT " + COLUNAS + " FROM empresa ORDER BY nome";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql);
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
        try (Connection connection = abrirConexao()) {
            connection.setAutoCommit(false);

            try {
                boolean atualizou = atualizarNaConexao(connection, empresa, endereco);
                if (atualizou) {
                    connection.commit();
                } else {
                    desfazer(connection);
                }
                return atualizou;

            } catch (SQLException e) {
                desfazer(connection);
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar empresa: " + e.getMessage());
            return false;
        }
    }

    private boolean atualizarNaConexao(Connection connection, Empresa empresa, Endereco endereco) throws SQLException {
        String sql = "UPDATE empresa SET nome = ?, descricao = ?, cnpj = ?, email_contato = ?, ativo = ?, url_foto = ? WHERE id = ?";

        endereco.setId(empresa.getIdEndereco());
        enderecoDAO.atualizarNaConexao(endereco, connection);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, empresa.getNome());
            stmt.setString(2, empresa.getDescricao());
            stmt.setString(3, empresa.getCnpj());
            stmt.setString(4, empresa.getEmailContato());
            stmt.setString(5, empresa.getStatus().name());
            stmt.setString(6, empresa.getUrlFoto());
            stmt.setInt(7, empresa.getId());

            return stmt.executeUpdate() > 0;
        }
    }
}
