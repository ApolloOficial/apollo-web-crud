package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import org.apollo.apollowebcrud.DAO.UsuarioDAO;
import org.apollo.apollowebcrud.model.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet ("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String cpf = request.getParameter("cpf");
        String senha = request.getParameter("senha");
        UsuarioDAO userD = new UsuarioDAO();
        List<Usuario> users = userD.listar();
        for (Usuario user : users){
            if(user.getEmail().equals(email) && user.getCpf().equals(cpf) && user.getSenha().equals(senha)){
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogado", user);
                response.sendRedirect("usuarios?action=telaInicial");
                return;
            }
        }
        request.setAttribute("ErroNoLogin",true);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
