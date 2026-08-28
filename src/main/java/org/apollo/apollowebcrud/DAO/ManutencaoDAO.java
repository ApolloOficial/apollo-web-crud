package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.Manutencao;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManutencaoDAO extends ApolloConnection {

    public ManutencaoDAO() {
        super();
    }

    public boolean inserir(Manutencao manutencao) {
        String sql = "INSERT INTO manutencao (id_painel, tipo, descricao, prazo) VALUES (?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, manutencao.getIdPainel());
            stmt.setString(2, manutencao.getTipo());
            stmt.setString(3, manutencao.getDescricao());
            stmt.setDate(4, Date.valueOf(manutencao.getPrazo()));
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ManutencaoDAO.class.getName()).log(Level.SEVERE, "SQL error on INSERT", ex);
            manutencao.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public List<Manutencao> listar() {
        String sql = "SELECT * FROM manutencao ORDER BY id";
        List<Manutencao> manutencoes = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet consulta = stmt.executeQuery();
            while (consulta.next()) {
                Manutencao manutencao = new Manutencao();
                manutencao.setId(consulta.getInt("id"));
                manutencao.setIdPainel(consulta.getInt("id_painel"));
                manutencao.setTipo(consulta.getString("tipo"));
                manutencao.setDescricao(consulta.getString("descricao"));
                manutencao.setPrazo(consulta.getDate("prazo").toLocalDate());
                manutencoes.add(manutencao);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManutencaoDAO.class.getName()).log(Level.SEVERE, "SQL error on SELECT", ex);
        }
        return manutencoes;
    }

    public boolean atualizar(Manutencao manutencao) {
        String sql = "UPDATE manutencao SET id_painel=?, tipo=?, descricao=?, prazo=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, manutencao.getIdPainel());
            stmt.setString(2, manutencao.getTipo());
            stmt.setString(3, manutencao.getDescricao());
            stmt.setDate(4, Date.valueOf(manutencao.getPrazo()));
            stmt.setInt(5, manutencao.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ManutencaoDAO.class.getName()).log(Level.SEVERE, "SQL error on UPDATE", ex);
            manutencao.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public boolean deletar(Manutencao manutencao) {
        String sql = "DELETE FROM manutencao WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, manutencao.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ManutencaoDAO.class.getName()).log(Level.SEVERE, "SQL error on DELETE", ex);
            manutencao.setMensagemErro(ex.getMessage());
            return false;
        }
    }
}