package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.SugestaoRealocacao;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SugestaoRealocacaoDAO extends ApolloConnection {

    public SugestaoRealocacaoDAO() {
        super();
    }

    public boolean inserir(SugestaoRealocacao sugestao) {
        String sql = "INSERT INTO sugestao_realocacao (id_painel, preco, justificativa, destino) VALUES (?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, sugestao.getIdPainel());
            stmt.setDouble(2, sugestao.getPreco());
            stmt.setString(3, sugestao.getJustificativa());
            stmt.setString(4, sugestao.getDestino());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SugestaoRealocacaoDAO.class.getName()).log(Level.SEVERE, "SQL error on INSERT", ex);
            sugestao.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public List<SugestaoRealocacao> listar() {
        String sql = "SELECT * FROM sugestao_realocacao ORDER BY id";
        List<SugestaoRealocacao> sugestoes = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet consulta = stmt.executeQuery();
            while (consulta.next()) {
                SugestaoRealocacao sugestao = new SugestaoRealocacao();
                sugestao.setId(consulta.getInt("id"));
                sugestao.setIdPainel(consulta.getInt("id_painel"));
                sugestao.setPreco(consulta.getDouble("preco"));
                sugestao.setJustificativa(consulta.getString("justificativa"));
                sugestao.setDestino(consulta.getString("destino"));
                sugestoes.add(sugestao);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SugestaoRealocacaoDAO.class.getName()).log(Level.SEVERE, "SQL error on SELECT", ex);
        }
        return sugestoes;
    }

    public boolean atualizar(SugestaoRealocacao sugestao) {
        String sql = "UPDATE sugestao_realocacao SET id_painel=?, preco=?, justificativa=?, destino=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, sugestao.getIdPainel());
            stmt.setDouble(2, sugestao.getPreco());
            stmt.setString(3, sugestao.getJustificativa());
            stmt.setString(4, sugestao.getDestino());
            stmt.setInt(5, sugestao.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SugestaoRealocacaoDAO.class.getName()).log(Level.SEVERE, "SQL error on UPDATE", ex);
            sugestao.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public boolean deletar(SugestaoRealocacao sugestao) {
        String sql = "DELETE FROM sugestao_realocacao WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, sugestao.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SugestaoRealocacaoDAO.class.getName()).log(Level.SEVERE, "SQL error on DELETE", ex);
            sugestao.setMensagemErro(ex.getMessage());
            return false;
        }
    }
}