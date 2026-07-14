package org.apollo.apollowebcrud.servlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsuarioDAO userD = new UsuarioDAO();
        Usuario user = new Usuario();
        user.setNome(request.getParameter("nome"));
        user.setEmail(request.getParameter("email"));
        user.setCpf(request.getParameter("cpf"));
        user.setSenha(request.getParameter("senha"));
        user.setTipo(request.getParameter("tipo"));

        if (userD.inserir(user)) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", user);
            request.getRequestDispatcher("/WEB-INF/views/telaInicialUsuario.jsp").forward(request, response);
        } else {
            String msgDB = user.getMensagemErro();
            String msg;
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                msg = "Seu email não foi informado";
            } else if (!user.getEmail().contains("mail.com") || !user.getEmail().contains("@")) {
                msg = "Seu email não é válido";
            } else if (user.getCpf() == null || user.getCpf().trim().isEmpty()) {
                msg = "Seu CPF não foi informado";
            } else if (msgDB.contains("usuario_cpf_key")){
                msg = "Este CPF já foi cadastrado no sistema";
            } else if (user.getSenha() == null || user.getSenha().trim().isEmpty()){
                msg = "Sua senha não foi informada";
            } else if (msgDB.contains("chk_senha_caractere")){
                msg = "Sua senha deve possuir pelo um destes caracteres especiais: [!@#$%*&]";
            } else if (msgDB.contains("chk_senha_tamanho")) {
                msg = "Sua senha deve possuir pelo menos 8 caracteres";
            } else if (user.getTipo() == null || user.getTipo().trim().isEmpty()) {
                msg = "Seu tipo não foi informado";
            } else {
                msg = "Ocorreu um erro no seu cadastro";
            }
            request.setAttribute("ErroNoCadastro",msg);
            request.getRequestDispatcher("/WEB-INF/views/cadastroTeste.jsp").forward(request, response);
        }
    }
}
