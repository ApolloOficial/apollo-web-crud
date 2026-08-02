package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.Lote;
import org.apollo.apollowebcrud.util.ApolloConnection;

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
            stmt.setInt(1, lote.getQuantidadePaineis());
            stmt.setInt(2, lote.getEficienciaGeral());
            stmt.setString(3, lote.getObservacao());
            stmt.setInt(4, lote.getIdFilial());
            stmt.setString(5, lote.getLocalizacao());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAO.class.getName()).log(Level.SEVERE, "SQL error on INSERT", ex);
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
                Lote lote = new Lote();
                lote.setId(consulta.getInt("id_lote"));
                lote.setQuantidadePaineis(consulta.getInt("quantia_paineis"));
                lote.setEficienciaGeral(consulta.getInt("eficiencia_geral"));
                lote.setObservacao(consulta.getString("observacao"));
                lote.setIdFilial(consulta.getInt("id_cadastro"));
                lote.setLocalizacao(consulta.getString("localizacao"));
                lotes.add(lote);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAO.class.getName()).log(Level.SEVERE, "SQL error on SELECT", ex);
        }
        return lotes;
    }
    public boolean atualizar(Lote lote){
        String sql = "UPDATE lote SET quantia_paineis=?, eficiencia_geral=?, observacao=?, id_cadastro=?, localizacao=? WHERE id_lote=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, lote.getQuantidadePaineis());
            stmt.setInt(2, lote.getEficienciaGeral());
            stmt.setString(3, lote.getObservacao());
            stmt.setInt(4, lote.getIdFilial());
            stmt.setString(5, lote.getLocalizacao());
            stmt.setInt(6, lote.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(LoteDAO.class.getName()).log(Level.SEVERE, "SQL error on UPDATE", ex);
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
            Logger.getLogger(LoteDAO.class.getName()).log(Level.SEVERE, "SQL error on DELETE", ex);
            return false;
        }
    }
}
