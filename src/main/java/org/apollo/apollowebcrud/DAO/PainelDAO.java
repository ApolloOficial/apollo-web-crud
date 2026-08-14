package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.Painel;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PainelDAO extends ApolloConnection {

    public PainelDAO() {
        super();
    }

    public boolean inserir(Painel painel) {
        String sql = "INSERT INTO painel (id, numero_serie, material, modelo, marca, nota_fiscal, eficiencia, vida_util, id_lote, tempo_uso) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, painel.getId());
            stmt.setString(2, painel.getNumeroSerie());
            stmt.setString(3, painel.getMaterial());
            stmt.setString(4, painel.getModelo());
            stmt.setString(5, painel.getMarca());
            stmt.setString(6, painel.getNotaFiscal());
            stmt.setDouble(7, painel.getEficiencia());
            stmt.setInt(8, painel.getVidaUtil());
            stmt.setInt(9, painel.getIdLote());
            stmt.setInt(10, painel.getTempoUso());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PainelDAO.class.getName()).log(Level.SEVERE, "SQL error on INSERT", ex);
            painel.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public List<Painel> listar() {
        String sql = "SELECT * FROM painel ORDER BY id";
        List<Painel> paineis = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet consulta = stmt.executeQuery();
            while (consulta.next()) {
                Painel painel = new Painel();
                painel.setId(consulta.getInt("id"));
                painel.setNumeroSerie(consulta.getString("numero_serie"));
                painel.setMaterial(consulta.getString("material"));
                painel.setModelo(consulta.getString("modelo"));
                painel.setMarca(consulta.getString("marca"));
                painel.setNotaFiscal(consulta.getString("nota_fiscal"));
                painel.setEficiencia(consulta.getDouble("eficiencia"));
                painel.setVidaUtil(consulta.getInt("vida_util"));
                painel.setIdLote(consulta.getInt("id_lote"));
                painel.setTempoUso(consulta.getInt("tempo_uso"));
                paineis.add(painel);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PainelDAO.class.getName()).log(Level.SEVERE, "SQL error on SELECT", ex);
        }
        return paineis;
    }

    public boolean atualizar(Painel painel) {
        String sql = "UPDATE painel SET numero_serie=?, material=?, modelo=?, marca=?, nota_fiscal=?, eficiencia=?, vida_util=?, id_lote=?, tempo_uso=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, painel.getNumeroSerie());
            stmt.setString(2, painel.getMaterial());
            stmt.setString(3, painel.getModelo());
            stmt.setString(4, painel.getMarca());
            stmt.setString(5, painel.getNotaFiscal());
            stmt.setDouble(6, painel.getEficiencia());
            stmt.setInt(7, painel.getVidaUtil());
            stmt.setInt(8, painel.getIdLote());
            stmt.setInt(9, painel.getTempoUso());
            stmt.setInt(10, painel.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PainelDAO.class.getName()).log(Level.SEVERE, "SQL error on UPDATE", ex);
            painel.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public boolean deletar(Painel painel) {
        String sql = "DELETE FROM painel WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, painel.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PainelDAO.class.getName()).log(Level.SEVERE, "SQL error on DELETE", ex);
            painel.setMensagemErro(ex.getMessage());
            return false;
        }
    }
}