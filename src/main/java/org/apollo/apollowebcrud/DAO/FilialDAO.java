package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.Filial;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilialDAO extends ApolloConnection {

    public FilialDAO() {
        super();
    }

    public boolean inserirFilial(Filial filial) {
        String sql = "INSERT INTO filial (id, id_empresa, localizacao) VALUES (?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, filial.getId());
            stmt.setInt(2, filial.getIdEmpresa());
            stmt.setString(3, filial.getLocalizacao());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FilialDAO.class.getName()).log(Level.SEVERE, "SQL error on INSERT", ex);
            filial.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public List<Filial> listar() {
        String sql = "SELECT * FROM filial ORDER BY id";
        List<Filial> filiais = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet consulta = stmt.executeQuery();
            while (consulta.next()) {
                Filial filial = new Filial();
                filial.setId(consulta.getInt("id"));
                filial.setIdEmpresa(consulta.getInt("id_empresa"));
                filial.setLocalizacao(consulta.getString("localizacao"));
                filiais.add(filial);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FilialDAO.class.getName()).log(Level.SEVERE, "SQL error on SELECT", ex);
        }
        return filiais;
    }

    public boolean atualizar(Filial filial) {
        String sql = "UPDATE filial SET id_empresa=?, localizacao=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, filial.getIdEmpresa());
            stmt.setString(2, filial.getLocalizacao());
            stmt.setInt(3, filial.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FilialDAO.class.getName()).log(Level.SEVERE, "SQL error on UPDATE", ex);
            filial.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public boolean deletar(Filial filial) {
        String sql = "DELETE FROM filial WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, filial.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FilialDAO.class.getName()).log(Level.SEVERE, "SQL error on DELETE", ex);
            filial.setMensagemErro(ex.getMessage());
            return false;
        }
    }
}