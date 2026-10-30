package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.service.RecuperacaoSenhaService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

// Rota pública. GET mostra a etapa 1 (e-mail) ou, com ?etapa=codigo&email=..., a etapa 2 (código + nova senha).
// POST com o campo "acao": enviar | redefinir
@WebServlet("/recuperar-senha")
public class RecuperacaoSenhaServlet extends HttpServlet {

    // Telas originais do colega: etapa 1 pede o e-mail; etapa 2 pede código e nova senha
    private static final String VIEW_ETAPA_EMAIL = "/WEB-INF/views/login_esqueci_senha.jsp";
    private static final String VIEW_ETAPA_CODIGO = "/WEB-INF/views/login_codigo.jsp";

    private final RecuperacaoSenhaService recuperacaoService = new RecuperacaoSenhaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String etapa = "codigo".equals(request.getParameter("etapa")) ? "codigo" : "email";
        mostrar(request, response, etapa, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("redefinir".equals(request.getParameter("acao"))) {
            redefinir(request, response);
        } else {
            enviar(request, response);
        }
    }

    private void enviar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String erro = recuperacaoService.solicitarCodigo(email);

        if (erro == null) {
            String emailCodificado = URLEncoder.encode(email.trim(), StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/recuperar-senha?etapa=codigo&email=" + emailCodificado);
            return;
        }
        mostrar(request, response, "email", erro);
    }

    private void redefinir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String erro = recuperacaoService.redefinirSenha(
                request.getParameter("email"),
                request.getParameter("codigo"),
                request.getParameter("novaSenha"),
                request.getParameter("confirmacaoSenha"));

        if (erro == null) {
            response.sendRedirect(request.getContextPath() + "/login?senhaRedefinida=1");
            return;
        }
        mostrar(request, response, "codigo", erro);
    }

    private void mostrar(HttpServletRequest request, HttpServletResponse response, String etapa, String erro)
            throws ServletException, IOException {
        request.setAttribute("etapa", etapa);
        request.setAttribute("emailRecuperacao", request.getParameter("email"));
        request.setAttribute("erroRecuperacao", erro);
        request.getRequestDispatcher("codigo".equals(etapa) ? VIEW_ETAPA_CODIGO : VIEW_ETAPA_EMAIL).forward(request, response);
    }
}
