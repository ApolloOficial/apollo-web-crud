package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Só contagens e somas das métricas da tela de detalhes da filial. Cada método recebe o id da filial.
public class ResumoFilialDAO extends ApolloConnection {

    private static final String DA_FILIAL = "JOIN string s ON s.id = p.id_string JOIN inversor i ON i.id = s.id_inversor WHERE i.id_filial = ?";

    public int contarInversores(int idFilial) {
        return contar("SELECT COUNT(*) FROM inversor WHERE id_filial = ? AND status = 'ATIVO'", idFilial);
    }

    public double somarPotenciaKw(int idFilial) {
        return somar("SELECT COALESCE(SUM(potencia_kw), 0) FROM inversor WHERE id_filial = ? AND status = 'ATIVO'", idFilial);
    }

    public int contarStrings(int idFilial) {
        return contar("SELECT COUNT(*) FROM string s JOIN inversor i ON i.id = s.id_inversor "
                + "WHERE i.id_filial = ? AND s.status = 'ATIVO'", idFilial);
    }

    public int contarPaineis(int idFilial) {
        return contar("SELECT COUNT(*) FROM painel p " + DA_FILIAL + " AND p.status IN ('ATIVO', 'INATIVO')", idFilial);
    }

    public int contarPaineisInativos(int idFilial) {
        return contar("SELECT COUNT(*) FROM painel p " + DA_FILIAL + " AND p.status = 'INATIVO'", idFilial);
    }

    public int contarManutencoesEmAberto(int idFilial) {
        return contar(manutencoes("m.status IN ('ABERTA', 'EM_ANDAMENTO')"), idFilial);
    }

    public int contarManutencoesEmAndamento(int idFilial) {
        return contar(manutencoes("m.status = 'EM_ANDAMENTO'"), idFilial);
    }

    private String manutencoes(String condicaoStatus) {
        return "SELECT COUNT(*) FROM manutencao m JOIN painel p ON p.id = m.id_painel "
                + DA_FILIAL + " AND " + condicaoStatus;
    }

    private int contar(String sql, int id) {
        return (int) somar(sql, id);
    }

    private double somar(String sql, int id) {
        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar o resumo da filial: " + e.getMessage());
        }
        return 0;
    }
}
