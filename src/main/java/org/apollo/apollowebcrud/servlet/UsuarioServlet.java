package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.model.Filial;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.FilialService;
import org.apollo.apollowebcrud.service.UsuarioService;
import org.apollo.apollowebcrud.util.ParametrosUtils;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Gerenciamento dos membros da equipe. O GET mostra a listagem ou, com editar, o formulário
 * preenchido (o cadastro de novos usuários fica em /cadastro). O POST aceita editar, inativar e reativar.
 * Os erros de edição seguem para a tela no atributo erroMembros.
 */
@WebServlet("/membros")
public class UsuarioServlet extends HttpServlet {

    private static final String VIEW_MEMBROS = "/WEB-INF/views/membros.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/cadastro-usuario.jsp";

    private final UsuarioService usuarioService = new UsuarioService();
    private final FilialService filialService = new FilialService();

    /** Abre a listagem de membros ou o formulário de edição. */
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

    /**
     * Executa a ação do campo acao. Sucesso redireciona para a lista e erro volta para a tela de origem.
     */
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
            response.sendRedirect(request.getContextPath() + "/membros?ok=" + acao);
            return;
        }

        request.setAttribute("erroMembros", erro);
        if (acao.equals("editar")) {
            mostrarFormulario(request, response, lerUsuario(request, id));
        } else {
            mostrarLista(request, response);
        }
    }

    /**
     * Carrega os membros visíveis, as filiais deles e os totais de técnicos, operadores e
     * filiais com equipe que aparecem no topo da tela.
     */
    private void mostrarLista(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);
        List<Usuario> membros = usuarioService.listarVisiveis(logado);
        Map<Integer, String> nomesFiliais = new HashMap<>();
        for (Filial filial : filialService.listarVisiveis(logado)) {
            nomesFiliais.put(filial.getId(), filial.getNome());
        }
        Set<Integer> filiaisComEquipe = new HashSet<>();
        int tecnicos = 0;
        int operadores = 0;
        for (Usuario membro : membros) {
            if (membro.getPerfil() == TipoUsuEnum.TECNICO) {
                tecnicos++;
            } else if (membro.getPerfil() == TipoUsuEnum.OPERADOR) {
                operadores++;
            }
            if (membro.getIdFilial() != null) {
                filiaisComEquipe.add(membro.getIdFilial());
            }
        }
        request.setAttribute("listaMembros", membros);
        request.setAttribute("nomesFiliais", nomesFiliais);
        request.setAttribute("totalTecnicos", tecnicos);
        request.setAttribute("totalOperadores", operadores);
        request.setAttribute("totalFiliaisComEquipe", filiaisComEquipe.size());
        request.getRequestDispatcher(VIEW_MEMBROS).forward(request, response);
    }

    /** Abre o formulário de edição com o usuário informado. */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Usuario item)
            throws ServletException, IOException {
        request.setAttribute("modo", "editar");
        request.setAttribute("item", item);
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    /** Lê nome, e-mail, CPF, telefone e foto do formulário de edição. */
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
