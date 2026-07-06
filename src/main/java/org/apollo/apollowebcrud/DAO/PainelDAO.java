package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.Painel;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PainelDAO extends ApolloConnection {
    private String table = "painel";
    public PainelDAO(){
        super();
    }

    public boolean inserir(Painel pnl){
        String sql = "INSERT INTO "+table+" (material, modelo, marca, eficiencia, vida_util, localizacao, id_lote) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, pnl.getMaterial());
            stmt.setString(2, pnl.getModelo());
            stmt.setString(3, pnl.getMarca());
            stmt.setInt(4, pnl.getEficiencia());
            stmt.setInt(5, pnl.getVida_util());
            stmt.setString(6, pnl.getLocalizacao());
            stmt.setInt(7, pnl.getId_lote());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PainelDAO.class.getName()).log(Level.SEVERE, "SQL error on INSERT", ex);
            return false;
        }
    }
    public List<Painel> listar(){
        String sql = "SELECT * FROM painel ORDER BY id_painel";
        List<Painel> paineis = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet consulta = stmt.executeQuery();
            while (consulta.next()){
                Painel pnls = new Painel();
                pnls.setId_painel(consulta.getInt("id_painel"));
                pnls.setMaterial(consulta.getString("material"));
                pnls.setModelo(consulta.getString("modelo"));
                pnls.setMarca(consulta.getString("marca"));
                pnls.setEficiencia(consulta.getInt("eficiencia"));
                pnls.setVida_util(consulta.getInt("vida_util"));
                pnls.setLocalizacao(consulta.getString("localizacao"));
                pnls.setId_lote(consulta.getInt("id_lote"));
                paineis.add(pnls);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PainelDAO.class.getName()).log(Level.SEVERE, "SQL error on SELECT", ex);
        }
        return paineis;
    }
    public boolean atualizar(Painel pnl){
        String sql = "UPDATE painel SET material=?, modelo=?, marca=?, eficiencia=?, vida_util=?, localizacao=?, id_lote=? WHERE id_painel=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, pnl.getMaterial());
            stmt.setString(2, pnl.getModelo());
            stmt.setString(3, pnl.getMarca());
            stmt.setInt(4, pnl.getEficiencia());
            stmt.setInt(5, pnl.getVida_util());
            stmt.setString(6, pnl.getLocalizacao());
            stmt.setInt(7, pnl.getId_lote());
            stmt.setInt( 8, pnl.getId_painel());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PainelDAO.class.getName()).log(Level.SEVERE, "SQL error on UPDATE", ex);
            return false;
        }
    }
    public boolean deletar(Painel pnl){
        String sql = "DELETE FROM painel WHERE id_painel=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pnl.getId_painel());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PainelDAO.class.getName()).log(Level.SEVERE, "SQL error on DELETE", ex);
            return false;
        }
    }
}
