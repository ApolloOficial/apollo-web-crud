package org.example.DAO;

import org.example.model.Leitura;
import org.example.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LeituraDAO extends ApolloConnection {

    public LeituraDAO(){
        super();
    }

    public boolean inserir(Leitura leitura){
        String sql = "INSERT INTO leitura (data, id_leitor, id_lote) VALUES(?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, leitura.getData());
            stmt.setInt(2, leitura.getId_leitor());
            stmt.setInt(3, leitura.getId_lote());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(LeituraDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public List<Leitura> listar(){
        String sql = "SELECT * FROM leitura ORDER BY id_leitura";
        List<Leitura> leituras = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet consulta = stmt.executeQuery();
            while (consulta.next()){
                Leitura leitura = new Leitura();
                leitura.setId(consulta.getInt("id_leitura"));
                leitura.setData(consulta.getObject("data", java.time.LocalDate.class));
                leitura.setId_leitor(consulta.getInt("id_leitor"));
                leitura.setId_lote(consulta.getInt("id_lote"));
                leituras.add(leitura);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LeituraDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Falha na conexão do banco de dados", ex);
        }
        return leituras;
    }
    public boolean atualizar(Leitura leitura){
        String sql = "UPDATE leitura SET data=?, id_leitor=?, id_lote=? WHERE id_leitura=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, leitura.getData());
            stmt.setInt(2, leitura.getId_leitor());
            stmt.setInt(3, leitura.getId_lote());
            stmt.setInt(4, leitura.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(LeituraDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public boolean deletar(Leitura leitura){
        String sql = "DELETE FROM leitura WHERE id_leitura=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, leitura.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(LeituraDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
