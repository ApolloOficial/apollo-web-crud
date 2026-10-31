package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.DashboardDAO;
import org.apollo.apollowebcrud.model.Dashboard;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;

/**
 * Calcula os números exibidos nos cards da página inicial, somando os dados vindos do DashboardDAO.
 */
public class DashboardService {

    private final DashboardDAO dashboardDAO = new DashboardDAO();

    /**
     * Escolhe o escopo certo para o perfil. O gerente enxerga a empresa inteira e os demais, apenas a filial.
     */
    public Dashboard montarDashboard(Usuario logado) {
        if (logado.getPerfil() == TipoUsuEnum.GERENTE && logado.getIdEmpresa() != null) {
            return dashboardDaEmpresa(logado.getIdEmpresa());
        }
        if (logado.getIdFilial() != null) {
            return dashboardDaFilial(logado.getIdFilial());
        }
        return new Dashboard();
    }

    /** Visão do gerente com membros, filiais, inversores e potência de toda a empresa. */
    private Dashboard dashboardDaEmpresa(int idEmpresa) {
        Dashboard dashboard = new Dashboard();
        dashboard.setTotalMembros(dashboardDAO.contarMembrosDaEmpresa(idEmpresa));
        dashboard.setTotalFiliais(dashboardDAO.contarFiliaisDaEmpresa(idEmpresa));
        dashboard.setTotalInversores(dashboardDAO.contarInversoresDaEmpresa(idEmpresa));
        dashboard.setPotenciaTotalKw(dashboardDAO.somarPotenciaDaEmpresa(idEmpresa));
        return dashboard;
    }

    /** Visão de técnico e operador, limitada à filial de cada um. */
    private Dashboard dashboardDaFilial(int idFilial) {
        Dashboard dashboard = new Dashboard();
        dashboard.setTotalMembros(dashboardDAO.contarMembrosDaFilial(idFilial));
        dashboard.setTotalManutencoes(dashboardDAO.contarManutencoesDaFilial(idFilial));
        dashboard.setTotalInversores(dashboardDAO.contarInversoresDaFilial(idFilial));
        dashboard.setPotenciaTotalKw(dashboardDAO.somarPotenciaDaFilial(idFilial));
        return dashboard;
    }
}
