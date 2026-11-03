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

/**
 * Cadastro de novos usuários. O gerente cadastra técnicos e o técnico cadastra operadores,
 * regra que fica no UsuarioService. O formulário não envia o tipo do usuário, o serviço
 * define esse valor de acordo com quem está cadastrando.
 * A tela recebe os atributos modo, filiaisDisponiveis e, quando há falha, ErroNoCadastro.
 */
@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {

    private static final String VIEW_CADASTRO = "/WEB-INF/views/cadastro-usuario.jsp";

    private final UsuarioService usuarioService = new UsuarioService();
    private final FilialService filialService = new FilialService();

    /** Exibe o formulário de cadastro vazio. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        prepararTela(request);
        request.getRequestDispatcher(VIEW_CADASTRO).forward(request, response);
    }

    /**
     * Tenta cadastrar o usuário com os dados enviados. Se der certo, redireciona para a lista
     * de membros. Caso contrário o formulário é exibido de novo com os valores digitados e a mensagem de erro.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario cadastrador = SessaoUtils.usuarioLogado(request);
        Usuario novo = lerFormulario(request);

        String erro = novo == null
                ? "O campo filial precisa ser um número"
                : usuarioService.cadastrarUsuario(novo, cadastrador);

        if (erro == null) {
            response.sendRedirect(request.getContextPath() + "/membros?ok=cadastrar");
            return;
        }

        request.setAttribute("ErroNoCadastro", erro);
        request.setAttribute("item", novo != null ? novo : lerFormularioSemFilial(request));
        prepararTela(request);
        request.getRequestDispatcher(VIEW_CADASTRO).forward(request, response);
    }

    /** Carrega o modo da tela e as filiais que o usuário logado pode escolher. */
    private void prepararTela(HttpServletRequest request) {
        request.setAttribute("modo", "novo");
        request.setAttribute("filiaisDisponiveis", filialService.listarVisiveis(SessaoUtils.usuarioLogado(request)));
    }

    /**
     * Monta o Usuario com os campos do formulário. Empresa e status não vêm da tela, a empresa é
     * a de quem cadastra e o status inicial é ATIVO. A senha digitada segue no campo senhaHash
     * e o serviço se encarrega de gerar o hash. Devolve null se a filial não for um número.
     */
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

    /** Mesma leitura do formulário, sem converter a filial. Serve para reexibir a tela após um erro. */
    private Usuario lerFormularioSemFilial(HttpServletRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.getParameter("nome"));
        usuario.setEmail(request.getParameter("email"));
        usuario.setCpf(request.getParameter("cpf"));
        usuario.setTelefone(request.getParameter("telefone"));
        usuario.setUrlFoto(request.getParameter("urlFoto"));
        return usuario;
    }

    /** Converte o texto em Integer e devolve null quando ele está vazio. */
    private Integer numeroOuNulo(String valor) {
        return valor == null || valor.isBlank() ? null : Integer.valueOf(valor.trim());
    }
}
