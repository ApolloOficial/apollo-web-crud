package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.FilialService;
import org.apollo.apollowebcrud.service.UsuarioService;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;

// Gerente cadastra técnico; técnico cadastra operador (a regra está no UsuarioService).
// O formulário NÃO envia o tipo: o Service define sozinho conforme quem está cadastrando.
// Atributos do formulário: modo ("novo"), filiaisDisponiveis (select de filial) e, no erro, ErroNoCadastro
@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {

    private static final String VIEW_CADASTRO = "/WEB-INF/views/Cadastro-Usuario.jsp";

    private final UsuarioService usuarioService = new UsuarioService();
    private final FilialService filialService = new FilialService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        prepararTela(request);
        request.getRequestDispatcher(VIEW_CADASTRO).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario cadastrador = SessaoUtils.usuarioLogado(request);
        Usuario novo = lerFormulario(request);

        String erro = novo == null
                ? "O campo filial precisa ser um número"
                : usuarioService.cadastrarUsuario(novo, cadastrador);

        if (erro == null) {
            response.sendRedirect(request.getContextPath() + "/membros");
            return;
        }

        request.setAttribute("ErroNoCadastro", erro);
        prepararTela(request);
        request.getRequestDispatcher(VIEW_CADASTRO).forward(request, response);
    }

    // Filiais que o usuário logado pode escolher no formulário
    private void prepararTela(HttpServletRequest request) {
        request.setAttribute("modo", "novo");
        request.setAttribute("filiaisDisponiveis", filialService.listarVisiveis(SessaoUtils.usuarioLogado(request)));
    }

    // O formulário não traz empresa nem status: a empresa vem de quem cadastra e o status começa ATIVO.
    // A senha digitada viaja no campo senhaHash e o Service transforma em hash.
    private Usuario lerFormulario(HttpServletRequest request) {
        try {
            Usuario novo = new Usuario();
            novo.setNome(request.getParameter("nome"));
            novo.setEmail(request.getParameter("email"));
            novo.setCpf(request.getParameter("cpf"));
            novo.setSenhaHash(request.getParameter("senha"));
            novo.setTelefone(request.getParameter("telefone"));
            novo.setUrlFoto(request.getParameter("urlFoto"));
            novo.setIdFilial(numeroOuNulo(request.getParameter("filial")));
            return novo;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer numeroOuNulo(String valor) {
        return valor == null || valor.isBlank() ? null : Integer.valueOf(valor.trim());
    }
}
