package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.model.Empresa;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.EmpresaService;
import org.apollo.apollowebcrud.service.FilialService;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;
import java.util.List;

/** Exibe os dados do usuário logado junto com a empresa e a filial a que ele pertence. */
@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    private static final String VIEW_PERFIL = "/WEB-INF/views/perfil.jsp";

    private final EmpresaService empresaService = new EmpresaService();
    private final FilialService filialService = new FilialService();

    /** Busca empresa e filial do usuário e abre a tela de perfil. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);
        List<Empresa> empresas = empresaService.listarVisiveis(logado);

        request.setAttribute("empresa", empresas.isEmpty() ? null : empresas.get(0));
        request.setAttribute("filial", logado.getIdFilial() == null ? null : filialService.buscarPorId(logado.getIdFilial()));
        request.getRequestDispatcher(VIEW_PERFIL).forward(request, response);
    }
}
