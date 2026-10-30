package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.DashboardDAO;
import org.apollo.apollowebcrud.model.Dashboard;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;

// Escolhe os números do dashboard conforme o perfil e o escopo (empresa ou filial) do usuário logado
public class DashboardService {

    private final DashboardDAO dashboardDAO = new DashboardDAO();

    public Dashboard montarDashboard(Usuario logado) {
        if (logado.getPerfil() == TipoUsuEnum.GERENTE && logado.getIdEmpresa() != null) {
            return dashboardDaEmpresa(logado.getIdEmpresa());
        }
        if (logado.getIdFilial() != null) {
            return dashboardDaFilial(logado.getIdFilial());
        }
        return new Dashboard();
    }

    // Gerente: empresa inteira
    private Dashboard dashboardDaEmpresa(int idEmpresa) {
        Dashboard dashboard = new Dashboard();
        dashboard.setTotalMembros(dashboardDAO.contarMembrosDaEmpresa(idEmpresa));
        dashboard.setTotalFiliais(dashboardDAO.contarFiliaisDaEmpresa(idEmpresa));
        dashboard.setTotalInversores(dashboardDAO.contarInversoresDaEmpresa(idEmpresa));
        dashboard.setPotenciaTotalKw(dashboardDAO.somarPotenciaDaEmpresa(idEmpresa));
        return dashboard;
    }

    // Técnico e operador: só a filial deles
    private Dashboard dashboardDaFilial(int idFilial) {
        Dashboard dashboard = new Dashboard();
        dashboard.setTotalMembros(dashboardDAO.contarMembrosDaFilial(idFilial));
        dashboard.setTotalManutencoes(dashboardDAO.contarManutencoesDaFilial(idFilial));
        dashboard.setTotalInversores(dashboardDAO.contarInversoresDaFilial(idFilial));
        dashboard.setPotenciaTotalKw(dashboardDAO.somarPotenciaDaFilial(idFilial));
        return dashboard;
    }
}
