package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Endereco;
import org.apollo.apollowebcrud.model.Filial;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilialDAO extends ApolloConnection {

    private final EnderecoDAO enderecoDAO = new EnderecoDAO();

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
        filial.setStatus(StatusGeral.valueOf(rs.getString("ativo")));
        filial.setIdUsuarioCadastro(rs.getObject("id_usuario_cadastro", Integer.class));
        return filial;
    }

// CREATE

    public int inserir(Filial filial, Endereco endereco) {
        try (Connection connection = abrirConexao()) {
            connection.setAutoCommit(false);

            try {
                int idGerado = inserirNaConexao(connection, filial, endereco);
                connection.commit();
                return idGerado;

            } catch (SQLException e) {
                desfazer(connection);
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir filial: " + e.getMessage());
            return -1;
        }
    }

    // grava endereço + filial na mesma transação; devolve o id gerado
    private int inserirNaConexao(Connection connection, Filial filial, Endereco endereco) throws SQLException {
        String sql = "INSERT INTO filial (id_empresa, id_segmento, nome, cnpj, email, telefone, id_endereco, ativo, id_usuario_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int idEndereco = enderecoDAO.inserirNaConexao(endereco, connection);
        filial.setIdEndereco(idEndereco);

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, filial.getIdEmpresa());
            stmt.setInt(2, filial.getIdSegmento());
            stmt.setString(3, filial.getNome());
            stmt.setString(4, filial.getCnpj());
            stmt.setString(5, filial.getEmail());
            stmt.setString(6, filial.getTelefone());
            stmt.setInt(7, filial.getIdEndereco());
            stmt.setString(8, StatusGeral.ATIVO.name());
            stmt.setObject(9, filial.getIdUsuarioCadastro(), Types.INTEGER);

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

    public Filial buscarPorId(int id) {
        String sql = "SELECT * FROM filial WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
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

    public Filial buscarPorCnpj(String cnpj) {
        String sql = "SELECT * FROM filial WHERE cnpj = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cnpj);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar filial por CNPJ: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    public List<Filial> listarTodos() {
        List<Filial> lista = new ArrayList<>();
        String sql = "SELECT * FROM filial ORDER BY nome";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql);
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
        try (Connection connection = abrirConexao()) {
            connection.setAutoCommit(false);

            try {
                boolean atualizou = atualizarNaConexao(connection, filial, endereco);
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
            System.err.println("Erro ao atualizar filial: " + e.getMessage());
            return false;
        }
    }

    private boolean atualizarNaConexao(Connection connection, Filial filial, Endereco endereco) throws SQLException {
        String sql = "UPDATE filial SET id_empresa = ?, id_segmento = ?, nome = ?, cnpj = ?, email = ?, telefone = ?, ativo = ? WHERE id = ?";

        endereco.setId(filial.getIdEndereco());
        enderecoDAO.atualizarNaConexao(endereco, connection);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, filial.getIdEmpresa());
            stmt.setInt(2, filial.getIdSegmento());
            stmt.setString(3, filial.getNome());
            stmt.setString(4, filial.getCnpj());
            stmt.setString(5, filial.getEmail());
            stmt.setString(6, filial.getTelefone());
            stmt.setString(7, filial.getStatus().name());
            stmt.setInt(8, filial.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    // READ filtrado
    public List<Filial> listarPorEmpresa(int idEmpresa) {
        List<Filial> lista = new ArrayList<>();
        String sql = "SELECT * FROM filial WHERE id_empresa = ? ORDER BY nome";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEmpresa);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar filiais da empresa: " + e.getMessage());
        }

        return lista;
    }
}
