package org.example.DAO;

import org.example.model.Usuario;
import org.example.util.ApolloConnection;

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
        String sql = "INSERT INTO usuario (nome, email, cpf, senha, tipo) VALUES(?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getCpf());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getTipo());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public List<Usuario> listar(){
        String sql = "SELECT * FROM usuario ORDER BY id_usuario";
        List<Usuario> usuarios = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet consulta = stmt.executeQuery();
            while (consulta.next()){
                Usuario usuario = new Usuario();
                usuario.setId_usuario(consulta.getInt("id_usuario"));
                usuario.setNome(consulta.getString("nome"));
                usuario.setEmail(consulta.getString("email"));
                usuario.setCpf(consulta.getString("cpf"));
                usuario.setSenha(consulta.getString("senha"));
                usuario.setTipo(consulta.getString("tipo"));
                usuarios.add(usuario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarios;
    }
    public boolean atualizar(Usuario usuario){
        String sql = "UPDATE usuario SET nome=?, email=?, cpf=?, senha=?, tipo=? WHERE id_usuario=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getCpf());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getTipo());
            stmt.setInt(6, usuario.getId_usuario());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public boolean deletar(Usuario usuario){
        String sql = "DELETE FROM usuario WHERE id_usuario=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, usuario.getId_usuario());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
