package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.service.ContratoService;
import org.apollo.apollowebcrud.service.EmpresaService;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;

// Só leitura (gerente). Atributos: contrato (Contrato, pode ser null) e empresa (Empresa)
@WebServlet("/contrato")
public class ContratoServlet extends HttpServlet {

    private static final String VIEW_CONTRATO = "/WEB-INF/views/contrato.jsp";

    private final ContratoService contratoService = new ContratoService();
    private final EmpresaService empresaService = new EmpresaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var logado = SessaoUtils.usuarioLogado(request);

        request.setAttribute("contrato", contratoService.buscarDaEmpresa(logado));
        request.setAttribute("empresa", empresaService.buscarDoGerente(logado));
        request.getRequestDispatcher(VIEW_CONTRATO).forward(request, response);
    }
}
