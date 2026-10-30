package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.UsuarioService;
import org.apollo.apollowebcrud.util.ParametrosUtils;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;

// GET /membros (listagem) | GET /membros?editar=ID (formulário preenchido; o cadastro novo fica em /cadastro)
// POST com o campo "acao": editar | inativar | reativar
// Formulário de edição: modo ("editar") e item (Usuario). Erro de edição: erroMembros
@WebServlet("/membros")
public class UsuarioServlet extends HttpServlet {

    private static final String VIEW_MEMBROS = "/WEB-INF/views/membros.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/Cadastro-Usuario.jsp";

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("editar") == null) {
            mostrarLista(request, response);
            return;
        }

        Usuario logado = SessaoUtils.usuarioLogado(request);
        Usuario item = usuarioService.buscarParaEdicao(ParametrosUtils.idOuZero(request.getParameter("editar")), logado);
        if (item == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        mostrarFormulario(request, response, item);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);
        String acao = request.getParameter("acao") == null ? "" : request.getParameter("acao");
        int id = ParametrosUtils.idOuZero(request.getParameter("id"));

        String erro = switch (acao) {
            case "editar" -> usuarioService.editarUsuario(lerUsuario(request, id), request.getParameter("senha"), logado);
            case "inativar" -> usuarioService.inativarUsuario(id, logado);
            case "reativar" -> usuarioService.reativarUsuario(id, logado);
            default -> "Ação inválida";
        };

        if (erro == null) {
            response.sendRedirect(request.getContextPath() + "/membros");
            return;
        }

        request.setAttribute("erroMembros", erro);
        if (acao.equals("editar")) {
            mostrarFormulario(request, response, lerUsuario(request, id));
        } else {
            mostrarLista(request, response);
        }
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("listaMembros", usuarioService.listarVisiveis(SessaoUtils.usuarioLogado(request)));
        request.getRequestDispatcher(VIEW_MEMBROS).forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Usuario item)
            throws ServletException, IOException {
        request.setAttribute("modo", "editar");
        request.setAttribute("item", item);
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private Usuario lerUsuario(HttpServletRequest request, int id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(request.getParameter("nome"));
        usuario.setEmail(request.getParameter("email"));
        usuario.setCpf(request.getParameter("cpf"));
        usuario.setTelefone(request.getParameter("telefone"));
        usuario.setUrlFoto(request.getParameter("urlFoto"));
        return usuario;
    }
}
