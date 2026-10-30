package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.UsuarioService;
import org.apollo.apollowebcrud.util.ParametrosUtils;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;
import java.util.Locale;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String VIEW_LOGIN = "/WEB-INF/views/login.jsp";

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("areaSelecionada", "gerente");
        request.getRequestDispatcher(VIEW_LOGIN).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        TipoUsuEnum areaSelecionada = ParametrosUtils.enumOuNulo(TipoUsuEnum.class, request.getParameter("area"));

        Usuario usuario = usuarioService.validarLogin(email, senha, areaSelecionada);

        if (usuario == null) {
            request.setAttribute("erroLogin", "Email, senha ou área de acesso incorretos.");
            request.setAttribute("emailDigitado", email);
            request.setAttribute("areaSelecionada", areaSelecionada == null
                    ? "gerente"
                    : areaSelecionada.name().toLowerCase(Locale.ROOT));
            request.getRequestDispatcher(VIEW_LOGIN).forward(request, response);
            return;
        }

        iniciarSessao(request, usuario);
        response.sendRedirect(request.getContextPath() + usuarioService.paginaInicial(usuario));
    }

    private void iniciarSessao(HttpServletRequest request, Usuario usuario) {
        HttpSession antiga = request.getSession(false);
        if (antiga != null) {
            antiga.invalidate();
        }
        request.getSession(true).setAttribute(SessaoUtils.USUARIO_LOGADO, usuario);
    }
}
