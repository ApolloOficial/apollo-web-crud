package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.UsuarioService;

import java.io.IOException;

@WebServlet ("/login")
public class LoginServlet extends HttpServlet {

    UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String cpf = request.getParameter("cpf");
        String senha = request.getParameter("senha");
        Usuario usuario = usuarioService.validarLogin(email, cpf, senha);
        if (usuario!=null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", usuario);
            response.sendRedirect("usuarios?action=telaInicial");
            return;
        }

        request.setAttribute("ErroNoLogin",true);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
