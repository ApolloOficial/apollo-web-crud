package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acesso à tabela usuario, usada no login, na revalidação da sessão e no gerenciamento da equipe. */
public class UsuarioDAO extends ApolloConnection {

    public UsuarioDAO() {
        super();
    }

    /** Lê as colunas da linha corrente e monta um usuário. */
    private Usuario montar(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
        usuario.setIdEmpresa(rs.getObject("id_empresa", Integer.class));
        usuario.setIdFilial(rs.getObject("id_filial", Integer.class));
        usuario.setEmail(rs.getString("email"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setCpf(rs.getString("cpf"));
        usuario.setStatus(rs.getString("ativo"));
        usuario.setUrlFoto(rs.getString("url_foto"));
        usuario.setIdUsuarioCadastro(rs.getObject("id_usuario_cadastro", Integer.class));
        return usuario;
    }

    // CREATE

    /** Insere um usuário na tabela. O retorno é o id criado e -1 indica erro. */
    public int inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, id_tipo_usuario, id_empresa, id_filial, email, telefone, cpf, senha_hash, ativo, url_foto, id_usuario_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // retorna o id gerado pelo banco
        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setInt(2, usuario.getIdTipoUsuario());
            stmt.setObject(3, usuario.getIdEmpresa(), Types.INTEGER);
            stmt.setObject(4, usuario.getIdFilial(), Types.INTEGER);
            stmt.setString(5, usuario.getEmail());
            stmt.setString(6, usuario.getTelefone());
            stmt.setString(7, usuario.getCpf());
            stmt.setString(8, usuario.getSenhaHash());
            stmt.setString(9, "ATIVO");
            stmt.setString(10, usuario.getUrlFoto());
            stmt.setObject(11, usuario.getIdUsuarioCadastro(), Types.INTEGER);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
            return -1;
        }
    }

    // READ por id

    /** Consulta um usuário pela chave primária, ou null se não houver registro. */
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }

    /**
     * Consulta usada pelo AuthFilter em cada requisição. Traz o perfil junto com o usuário e lança
     * SQLException em caso de falha, para que o filtro diferencie banco indisponível de usuário inexistente.
     */
    public Usuario buscarParaRevalidarSessao(int id) throws SQLException {
        String sql = "SELECT u.*, t.nome AS perfil_sessao FROM usuario u "
                + "JOIN tipo_usuario t ON t.id = u.id_tipo_usuario WHERE u.id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Usuario usuario = montar(rs);
                String perfil = rs.getString("perfil_sessao");
                if (perfil != null) {
                    try {
                        usuario.setPerfil(TipoUsuEnum.valueOf(perfil));
                    } catch (IllegalArgumentException ignorado) {
                        usuario.setPerfil(null);
                    }
                }
                return usuario;
            }
        }
    }

    /** Procura pelo CPF já sem máscara, para impedir duplicidade. */
    public Usuario buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM usuario WHERE cpf = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }

    /** Procura pelo e-mail, que também é único por usuário. */
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }

    // READ todos

    /** Lista todos os registros de usuário. */
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY nome";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE

    /** Grava as alterações de um usuário e informa se o registro foi encontrado e atualizado. */
    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, id_tipo_usuario = ?, id_empresa = ?, id_filial = ?, email = ?, telefone = ?, cpf = ?, ativo = ?, url_foto = ? WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setInt(2, usuario.getIdTipoUsuario());
            stmt.setObject(3, usuario.getIdEmpresa(), Types.INTEGER);
            stmt.setObject(4, usuario.getIdFilial(), Types.INTEGER);
            stmt.setString(5, usuario.getEmail());
            stmt.setString(6, usuario.getTelefone());
            stmt.setString(7, usuario.getCpf());
            stmt.setString(8, usuario.getStatus());
            stmt.setString(9, usuario.getUrlFoto());
            stmt.setInt(10, usuario.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }

    // BUSCA PARA LOGIN (traz o hash para o Service conferir)

    /** Busca o usuário pelo e-mail, trazendo o hash da senha para a conferência do login. */
    public Usuario buscarParaLogin(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = montar(rs);
                    usuario.setSenhaHash(rs.getString("senha_hash"));
                    return usuario;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário para login: " + e.getMessage());
        }

        return null;
    }

    /** Lista os usuários de uma empresa com o tipo informado, ordenados pelo nome. */
    public List<Usuario> listarPorEmpresaETipo(int idEmpresa, String tipo) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.* FROM usuario u JOIN tipo_usuario t ON t.id = u.id_tipo_usuario WHERE u.id_empresa = ? AND t.nome = ? ORDER BY u.nome";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEmpresa);
            stmt.setString(2, tipo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários da empresa: " + e.getMessage());
        }

        return lista;
    }

    /** Faz a mesma listagem, mas dentro de uma filial. */
    public List<Usuario> listarPorFilialETipo(int idFilial, String tipo) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.* FROM usuario u JOIN tipo_usuario t ON t.id = u.id_tipo_usuario WHERE u.id_filial = ? AND t.nome = ? ORDER BY u.nome";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFilial);
            stmt.setString(2, tipo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários da filial: " + e.getMessage());
        }

        return lista;
    }

    /**
     * O gerente recebe a empresa que acabou de cadastrar, mas só se ainda não estiver vinculado a nenhuma.
     */
    public boolean vincularEmpresaNaConexao(Connection connection, int idUsuario, int idEmpresa) throws SQLException {
        String sql = "UPDATE usuario SET id_empresa = ? WHERE id = ? AND id_empresa IS NULL";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEmpresa);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate() > 0;
        }
    }

    /** Troca apenas a senha, que já deve chegar em forma de hash. */
    public boolean atualizarSenha(int id, String senhaHash) {
        String sql = "UPDATE usuario SET senha_hash = ? WHERE id = ?";

        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, senhaHash);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar senha: " + e.getMessage());
            return false;
        }
    }
}
