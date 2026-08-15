package org.apollo.apollowebcrud.DAO;

import org.apollo.apollowebcrud.model.Empresa;
import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmpresaDAO extends ApolloConnection {

    public EmpresaDAO() {
        super();
    }

    public boolean inserir(Empresa empresa) {
        String sql = "INSERT INTO empresa (id, nome) VALUES (?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, empresa.getId());
            stmt.setString(2, empresa.getNome());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(EmpresaDAO.class.getName()).log(Level.SEVERE, "SQL error on INSERT", ex);
            empresa.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public List<Empresa> listar() {
        String sql = "SELECT * FROM empresa ORDER BY id";
        List<Empresa> empresas = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet consulta = stmt.executeQuery();
            while (consulta.next()) {
                Empresa empresa = new Empresa();
                empresa.setId(consulta.getInt("id"));
                empresa.setNome(consulta.getString("nome"));
                empresas.add(empresa);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmpresaDAO.class.getName()).log(Level.SEVERE, "SQL error on SELECT", ex);
        }
        return empresas;
    }

    public boolean atualizar(Empresa empresa) {
        String sql = "UPDATE empresa SET nome=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, empresa.getNome());
            stmt.setInt(2, empresa.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(EmpresaDAO.class.getName()).log(Level.SEVERE, "SQL error on UPDATE", ex);
            empresa.setMensagemErro(ex.getMessage());
            return false;
        }
    }

    public boolean deletar(Empresa empresa) {
        String sql = "DELETE FROM empresa WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, empresa.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(EmpresaDAO.class.getName()).log(Level.SEVERE, "SQL error on DELETE", ex);
            empresa.setMensagemErro(ex.getMessage());
            return false;
        }
    }
}