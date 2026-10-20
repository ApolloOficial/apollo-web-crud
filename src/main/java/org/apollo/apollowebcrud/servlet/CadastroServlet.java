package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.dao.FilialDAO;
import org.apollo.apollowebcrud.model.Filial;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.UsuarioService;

import java.io.IOException;
import java.util.List;

@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {

    UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Busca a lista de filiais do banco
        FilialDAO filialDAO = new FilialDAO();
        List<Filial> listaFiliais = filialDAO.listarTodos();
        request.setAttribute("filiaisDisponiveis", listaFiliais);
        request.getRequestDispatcher("cadastro-usuario.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String cpf = request.getParameter("cpf");
        String senha = request.getParameter("senha");
        String telefone = request.getParameter("telefone");
        String status = request.getParameter("status");
        String urlFoto = request.getParameter("urlFoto");
        int tipo = Integer.parseInt(request.getParameter("tipo"));
        int empresa = Integer.parseInt(request.getParameter("empresa"));
        int filial = Integer.parseInt(request.getParameter("filial"));

        Usuario usuarioDados = new Usuario(0, nome, tipo, empresa, filial, email, telefone, cpf, senha, status, urlFoto);

        String msgErro = usuarioService.cadastrarUsuario(usuarioDados);

        if (msgErro == null) {
            response.sendRedirect("usuarios?action=telaInicial");
            return;
        }
        request.setAttribute("ErroNoCadastro",msgErro);
        request.getRequestDispatcher("/WEB-INF/views/cadastroTeste.jsp").forward(request, response);
    }
}
