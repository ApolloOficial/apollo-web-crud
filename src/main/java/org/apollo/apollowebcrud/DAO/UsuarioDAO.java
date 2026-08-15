package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO extends ApolloConnection {

    public UsuarioDAO(){
        super();
    }

    public boolean inserir(Usuario usuario){
        String sql = "INSERT INTO usuario (id, nome, id_tipo_usuario, id_empresa, email, telefone, cpf, senha) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, usuario.getId());
            stmt.setString(2, usuario.getNome());
            stmt.setInt(3, usuario.getIdTipoUsuario());
            stmt.setInt(4, usuario.getIdEmpresa());
            stmt.setString(5, usuario.getEmail());
            stmt.setString(6, usuario.getTelefone());
            stmt.setString(7, usuario.getCpf());
            stmt.setString(8, usuario.getSenha());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, "SQL error on INSERT", ex);
            usuario.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public List<Usuario> listar(){
        String sql = "SELECT * FROM usuario ORDER BY id";
        List<Usuario> usuarios = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet consulta = stmt.executeQuery();
            while (consulta.next()){
                Usuario usuario = new Usuario();
                usuario.setId(consulta.getInt("id"));
                usuario.setNome(consulta.getString("nome"));
                usuario.setIdTipoUsuario(consulta.getInt("id_tipo_usuario"));
                usuario.setIdEmpresa(consulta.getInt("id_empresa"));
                usuario.setEmail(consulta.getString("email"));
                usuario.setTelefone(consulta.getString("telefone"));
                usuario.setCpf(consulta.getString("cpf"));
                usuario.setSenha(consulta.getString("senha"));
                usuarios.add(usuario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, "SQL error on SELECT", ex);
        }
        return usuarios;
    }

    public boolean atualizar(Usuario usuario){
        String sql = "UPDATE usuario SET nome=?, email=?, telefone=?, cpf=?, senha=? WHERE id=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getCpf());
            stmt.setString(5, usuario.getSenha());
            stmt.setInt(6, usuario.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, "SQL error on UPDATE", ex);
            usuario.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public boolean deletar(Usuario usuario){
        String sql = "DELETE FROM usuario WHERE id=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, usuario.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, "SQL error on DELETE", ex);
            usuario.setMensagemErro(ex.getMessage());
            return false;
        }
    }
}