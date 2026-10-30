package org.apollo.apollowebcrud.dao;

import org.apollo.apollowebcrud.util.ApolloConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Só consultas de contagem/soma para os cards do dashboard. Conta apenas registros ATIVOS
// (manutenção: tudo que não foi cancelado). Cada método recebe o id da empresa ou da filial.
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

    // ---------- AUXILIARES ----------

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
            System.err.println("Erro ao consultar dados do dashboard: " + e.getMessage());
        }
        return 0;
    }
}
