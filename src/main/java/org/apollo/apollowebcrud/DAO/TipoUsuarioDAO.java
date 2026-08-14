package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.TipoUsuario;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TipoUsuarioDAO extends ApolloConnection {

    public TipoUsuarioDAO() {
        super();
    }

    public boolean inserir(TipoUsuario tipoUsuario) {
        String sql = "INSERT INTO tipo_usuario (id, tipo, descricao) VALUES (?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tipoUsuario.getId());
            stmt.setString(2, tipoUsuario.getTipo());
            stmt.setString(3, tipoUsuario.getDescricao());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TipoUsuarioDAO.class.getName()).log(Level.SEVERE, "SQL error on INSERT", ex);
            tipoUsuario.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public List<TipoUsuario> listar() {
        String sql = "SELECT * FROM tipo_usuario ORDER BY id";
        List<TipoUsuario> tiposUsuario = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet consulta = stmt.executeQuery();
            while (consulta.next()) {
                TipoUsuario tipoUsuario = new TipoUsuario();
                tipoUsuario.setId(consulta.getInt("id"));
                tipoUsuario.setTipo(consulta.getString("tipo"));
                tipoUsuario.setDescricao(consulta.getString("descricao"));
                tiposUsuario.add(tipoUsuario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoUsuarioDAO.class.getName()).log(Level.SEVERE, "SQL error on SELECT", ex);
        }
        return tiposUsuario;
    }

    public boolean atualizar(TipoUsuario tipoUsuario) {
        String sql = "UPDATE tipo_usuario SET tipo=?, descricao=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, tipoUsuario.getTipo());
            stmt.setString(2, tipoUsuario.getDescricao());
            stmt.setInt(3, tipoUsuario.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TipoUsuarioDAO.class.getName()).log(Level.SEVERE, "SQL error on UPDATE", ex);
            tipoUsuario.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public boolean deletar(TipoUsuario tipoUsuario) {
        String sql = "DELETE FROM tipo_usuario WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tipoUsuario.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TipoUsuarioDAO.class.getName()).log(Level.SEVERE, "SQL error on DELETE", ex);
            tipoUsuario.setMensagemErro(ex.getMessage());
            return false;
        }
    }
}