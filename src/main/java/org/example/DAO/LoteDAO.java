package org.example.DAO;

import org.example.model.Lote;
import org.example.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoteDAO extends ApolloConnection {

    public LoteDAO(){
        super();
    }

    public boolean inserir(Lote lote){
        String sql = "INSERT INTO lote (quantia_paineis, eficiencia_geral, observacao, id_cadastro, localizacao) VALUES(?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, lote.getQuantia_paineis());
            stmt.setInt(2, lote.getEficiencia_geral());
            stmt.setString(3, lote.getObservacao());
            stmt.setInt(4, lote.getId_cadastro());
            stmt.setString(5, lote.getLocalizacao());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public List<Lote> listar(){
        String sql = "SELECT * FROM lote ORDER BY id_lote";
        List<Lote> lotes = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet consulta = stmt.executeQuery();
            while (consulta.next()){
                Lote lote = new Lote(0, 0, 0, null, 0, null);
                lote.setId(consulta.getInt("id_lote"));
                lote.setQuantia_paineis(consulta.getInt("quantia_paineis"));
                lote.setEficiencia_geral(consulta.getInt("eficiencia_geral"));
                lote.setObservacao(consulta.getString("observacao"));
                lote.setId_cadastro(consulta.getInt("id_cadastro"));
                lote.setLocalizacao(consulta.getString("localizacao"));
                lotes.add(lote);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lotes;
    }
    public boolean atualizar(Lote lote){
        String sql = "UPDATE lote SET quantia_paineis=?, eficiencia_geral=?, observacao=?, id_cadastro=?, localizacao=? WHERE id_lote=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, lote.getQuantia_paineis());
            stmt.setInt(2, lote.getEficiencia_geral());
            stmt.setString(3, lote.getObservacao());
            stmt.setInt(4, lote.getId_cadastro());
            stmt.setString(5, lote.getLocalizacao());
            stmt.setInt(6, lote.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public boolean deletar(Lote lote){
        String sql = "DELETE FROM lote WHERE id_lote=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, lote.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
