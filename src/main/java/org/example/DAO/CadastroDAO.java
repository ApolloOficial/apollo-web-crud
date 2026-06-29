package org.example.DAO;

import org.example.model.Cadastro;
import org.example.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CadastroDAO extends ApolloConnection {

    public CadastroDAO(){
        super();
    }

    public boolean inserir(Cadastro cads){
        String sql = "INSERT INTO cadastro (data, info_foto, id_cadastrante) VALUES(?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, cads.getData());
            stmt.setString(2, cads.getInfo_foto());
            stmt.setInt(3, cads.getId_cadastrante());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CadastroDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public List<Cadastro> listar(){
        String sql = "SELECT * FROM cadastro ORDER BY id_cadastro";
        List<Cadastro> cadastros = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet consulta = stmt.executeQuery();
            while (consulta.next()){
                Cadastro cads = new Cadastro();
                cads.setId(consulta.getInt("id_cadastro"));
                cads.setData(consulta.getObject("data", java.time.LocalDate.class));
                cads.setInfo_foto(consulta.getString("info_foto"));
                cads.setId_cadastrante(consulta.getInt("id_cadastrante"));
                cadastros.add(cads);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CadastroDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cadastros;
    }
    public boolean atualizar(Cadastro cadastro){
        String sql = "UPDATE cadastro SET data=?, info_foto=?, id_cadastrante=? WHERE id_cadastro=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, cadastro.getData());
            stmt.setString(2, cadastro.getInfo_foto());
            stmt.setInt(3, cadastro.getId_cadastrante());
            stmt.setInt(4, cadastro.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CadastroDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public boolean deletar(Cadastro cadastro){
        String sql = "DELETE FROM cadastro WHERE id_cadastro=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cadastro.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CadastroDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
