package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.SugestaoRevenda;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SugestaoRevendaDAO extends ApolloConnection {

    public SugestaoRevendaDAO(){
        super();
    }

    public boolean inserir(SugestaoRevenda sugestao){
        String sql = "INSERT INTO sugestao_revenda (id_painel, preco) VALUES(?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,sugestao.getId_painel());
            stmt.setDouble(2,sugestao.getPreco());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SugestaoRevendaDAO.class.getName()).log(Level.SEVERE, "SQL error on INSERT", ex);
            return false;
        }
    }
    public List<SugestaoRevenda> listar(){
        String sql = "SELECT * FROM sugestao_revenda ORDER BY id";
        List<SugestaoRevenda> sugestoesRevendas = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet consulta = stmt.executeQuery();
            while (consulta.next()){
                SugestaoRevenda sugestao = new SugestaoRevenda();
                sugestao.setId(consulta.getInt("id"));
                sugestao.setId_painel(consulta.getInt("id_painel"));
                sugestao.setPreco(consulta.getDouble("preco"));
                sugestoesRevendas.add(sugestao);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SugestaoRevendaDAO.class.getName()).log(Level.SEVERE, "SQL error on SELECT", ex);
        }
        return sugestoesRevendas;
    }
    public boolean atualizar(SugestaoRevenda sugestao){
        String sql = "UPDATE sugestao_revenda SET id_painel=?, preco=? WHERE id=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, sugestao.getId_painel());
            stmt.setDouble(2, sugestao.getPreco());
            stmt.setInt(3, sugestao.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SugestaoRevendaDAO.class.getName()).log(Level.SEVERE, "SQL error on UPDATE", ex);
            return false;
        }
    }
    public boolean deletar(SugestaoRevenda sugestao){
        String sql = "DELETE FROM sugestao_revenda WHERE id=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, sugestao.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SugestaoRevendaDAO.class.getName()).log(Level.SEVERE, "SQL error on DELETE", ex);
            return false;
        }
    }
}
