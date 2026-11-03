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

/**
 * Fluxo público de recuperação de senha em duas etapas. O GET mostra a etapa do e-mail ou,
 * com etapa=codigo e o e-mail na URL, a etapa do código e da nova senha.
 * O POST usa o campo acao, que vale enviar ou redefinir.
 */
@WebServlet("/recuperar-senha")
public class RecuperacaoSenhaServlet extends HttpServlet {

    // Etapa 1 pede o e-mail e a etapa 2 pede o código e a nova senha
    private static final String VIEW_ETAPA_EMAIL = "/WEB-INF/views/login_esqueci_senha.jsp";
    private static final String VIEW_ETAPA_CODIGO = "/WEB-INF/views/login_codigo.jsp";

    private final RecuperacaoSenhaService recuperacaoService = new RecuperacaoSenhaService();

    /** Decide qual das duas etapas será exibida. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String etapa = "codigo".equals(request.getParameter("etapa")) ? "codigo" : "email";
        mostrar(request, response, etapa, null);
    }

    /** Chama a redefinição quando acao for redefinir e, em qualquer outro caso, o envio do código. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("redefinir".equals(request.getParameter("acao"))) {
            redefinir(request, response);
        } else {
            enviar(request, response);
        }
    }

    /**
     * Primeira etapa. Pede ao serviço que gere o código e o envie por e-mail e, se não houver erro,
     * avança para a tela do código.
     */
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

    /**
     * Segunda etapa. Confere o código e grava a nova senha. Com sucesso volta ao login avisando
     * que a senha foi redefinida.
     */
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

    /** Exibe a etapa indicada, levando junto o e-mail digitado e a mensagem de erro, se existir. */
    private void mostrar(HttpServletRequest request, HttpServletResponse response, String etapa, String erro)
            throws ServletException, IOException {
        request.setAttribute("etapa", etapa);
        request.setAttribute("emailRecuperacao", request.getParameter("email"));
        request.setAttribute("erroRecuperacao", erro);
        request.getRequestDispatcher("codigo".equals(etapa) ? VIEW_ETAPA_CODIGO : VIEW_ETAPA_EMAIL).forward(request, response);
    }
}
