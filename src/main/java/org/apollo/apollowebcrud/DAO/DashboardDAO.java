package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Consultas de contagem e soma para os cards do dashboard. Só entram registros ATIVOS e,
 * no caso das manutenções, tudo que não foi cancelado. Os métodos recebem o id da empresa ou da filial.
 */
public class DashboardDAO extends ApolloConnection {

    // ---------- EMPRESA (visão do gerente) ----------

    public int contarMembrosDaEmpresa(int idEmpresa) {
        return contar("SELECT COUNT(*) FROM usuario WHERE id_empresa = ? AND ativo = 'ATIVO'", idEmpresa);
    }

    public int contarFiliaisDaEmpresa(int idEmpresa) {
        return contar("SELECT COUNT(*) FROM filial WHERE id_empresa = ? AND ativo = 'ATIVO'", idEmpresa);
    }

    public int contarInversoresDaEmpresa(int idEmpresa) {
        return contar("SELECT COUNT(*) FROM inversor i JOIN filial f ON f.id = i.id_filial "
                + "WHERE f.id_empresa = ? AND i.status = 'ATIVO'", idEmpresa);
    }

    public double somarPotenciaDaEmpresa(int idEmpresa) {
        return somar("SELECT COALESCE(SUM(i.potencia_kw), 0) FROM inversor i JOIN filial f ON f.id = i.id_filial "
                + "WHERE f.id_empresa = ? AND i.status = 'ATIVO'", idEmpresa);
    }

    /** Conta manutenções abertas ou em andamento em todas as filiais da empresa. */
    public int contarManutencoesPendentesDaEmpresa(int idEmpresa) {
        return contar("SELECT COUNT(*) FROM manutencao m "
                + "JOIN painel p ON p.id = m.id_painel "
                + "JOIN string s ON s.id = p.id_string "
                + "JOIN inversor i ON i.id = s.id_inversor "
                + "JOIN filial f ON f.id = i.id_filial "
                + "WHERE f.id_empresa = ? AND m.status IN ('ABERTA', 'EM_ANDAMENTO')", idEmpresa);
    }

    // ---------- FILIAL (visão do técnico e do operador) ----------

    public int contarMembrosDaFilial(int idFilial) {
        return contar("SELECT COUNT(*) FROM usuario WHERE id_filial = ? AND ativo = 'ATIVO'", idFilial);
    }

    public int contarInversoresDaFilial(int idFilial) {
        return contar("SELECT COUNT(*) FROM inversor WHERE id_filial = ? AND status = 'ATIVO'", idFilial);
    }

    public double somarPotenciaDaFilial(int idFilial) {
        return somar("SELECT COALESCE(SUM(potencia_kw), 0) FROM inversor WHERE id_filial = ? AND status = 'ATIVO'", idFilial);
    }

    public int contarManutencoesDaFilial(int idFilial) {
        return contar("SELECT COUNT(*) FROM manutencao m "
                + "JOIN painel p ON p.id = m.id_painel "
                + "JOIN string s ON s.id = p.id_string "
                + "JOIN inversor i ON i.id = s.id_inversor "
                + "WHERE i.id_filial = ? AND m.status <> 'CANCELADA'", idFilial);
    }

    /** Conta manutenções abertas ou em andamento na filial do usuário. */
    public int contarManutencoesPendentesDaFilial(int idFilial) {
        return contar("SELECT COUNT(*) FROM manutencao m "
                + "JOIN painel p ON p.id = m.id_painel "
                + "JOIN string s ON s.id = p.id_string "
                + "JOIN inversor i ON i.id = s.id_inversor "
                + "WHERE i.id_filial = ? AND m.status IN ('ABERTA', 'EM_ANDAMENTO')", idFilial);
    }

    // ---------- AUXILIARES ----------

    /** Executa uma consulta de contagem com o id como único parâmetro. */
    private int contar(String sql, int id) {
        return (int) somar(sql, id);
    }

    /** Executa uma consulta que devolve um valor numérico, ou zero em caso de erro. */
    private double somar(String sql, int id) {
        try (Connection connection = abrirConexao(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar dados do dashboard: " + e.getMessage());
        }
        return 0;
    }
}
