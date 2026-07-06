package org.apollo.apollowebcrud.servlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import org.apollo.apollowebcrud.DAO.UsuarioDAO;
import org.apollo.apollowebcrud.model.Usuario;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        /*response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<h1>Olá Mundo</h1>");
        out.close();*/
        /*request.getRequestDispatcher("/WEB-INF/views/teste.html").forward(request,response);*/
        UsuarioDAO userD = new UsuarioDAO();
        request.setAttribute("usuarios",userD.listar());
        request.getRequestDispatcher("/WEB-INF/views/cadastroTeste.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UsuarioDAO userD = new UsuarioDAO();
        Usuario usuario = new Usuario();
        usuario.setNome(request.getParameter("nome"));
        usuario.setEmail(request.getParameter("email"));
        usuario.setCpf(request.getParameter("cpf"));
        usuario.setSenha(request.getParameter("senha"));
        usuario.setTipo(request.getParameter("tipo"));

        userD.inserir(usuario);
        response.sendRedirect("usuarios");
    }
}
