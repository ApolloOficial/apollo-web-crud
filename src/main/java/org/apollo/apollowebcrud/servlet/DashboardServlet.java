package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.DashboardService;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;

// Gerente abre dashboard-gerente.jsp; técnico e operador abrem dashboard-tecnico.jsp (telas originais do colega).
// Atributos para a view: "dashboard" (os números dos cards) e "visaoGerente" (true = cards de empresa;
// false = cards de filial)
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final String VIEW_GERENTE = "/WEB-INF/views/dashboard-gerente.jsp";
    private static final String VIEW_FILIAL = "/WEB-INF/views/dashboard-tecnico.jsp";

    private final DashboardService dashboardService = new DashboardService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);

        boolean visaoGerente = logado.getPerfil() == TipoUsuEnum.GERENTE;

        request.setAttribute("dashboard", dashboardService.montarDashboard(logado));
        request.setAttribute("visaoGerente", visaoGerente);
        request.getRequestDispatcher(visaoGerente ? VIEW_GERENTE : VIEW_FILIAL).forward(request, response);
    }
}
