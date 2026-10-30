package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends ApolloConnection {

    public UsuarioDAO() {
        super();
    }

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

    // Leitura específica do principal da sessão; a exceção diferencia falha de banco de usuário ausente.
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

    // READ filtrado
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

    // READ filtrado
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

    // Liga o gerente à empresa que ele acabou de cadastrar (só se ele ainda não tiver empresa)
    public boolean vincularEmpresaNaConexao(Connection connection, int idUsuario, int idEmpresa) throws SQLException {
        String sql = "UPDATE usuario SET id_empresa = ? WHERE id = ? AND id_empresa IS NULL";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEmpresa);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate() > 0;
        }
    }

    // Troca só a senha (já em hash)
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
