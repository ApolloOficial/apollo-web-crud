package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/** Encerra a sessão do usuário e o devolve para a tela de login. */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /** Invalida a sessão atual. Só aceita POST para que um link externo não deslogue ninguém. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            sessao.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login");
    }
}