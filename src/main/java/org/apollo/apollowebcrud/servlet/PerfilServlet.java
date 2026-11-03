package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.model.Empresa;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.EmpresaService;
import org.apollo.apollowebcrud.service.FilialService;
import org.apollo.apollowebcrud.service.UsuarioService;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;
import java.util.List;

/**
 * Exibe os dados do usuário logado junto com a empresa e a filial a que ele pertence.
 * Também permite que o próprio usuário altere seus dados pessoais sem passar pela tela de membros.
 */
@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    private static final String VIEW_PERFIL = "/WEB-INF/views/perfil.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/cadastro-usuario.jsp";

    private final EmpresaService empresaService = new EmpresaService();
    private final FilialService filialService = new FilialService();
    private final UsuarioService usuarioService = new UsuarioService();

    /**
     * Abre a tela de perfil ou o formulário de edição, conforme o parâmetro editar.
     * O usuário editado é sempre o da sessão, então não é necessário receber um id pela URL.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario logado = SessaoUtils.usuarioLogado(request);

        if (request.getParameter("editar") != null) {
            mostrarFormulario(request, response, logado);
            return;
        }

        List<Empresa> empresas = empresaService.listarVisiveis(logado);

        request.setAttribute("empresa", empresas.isEmpty() ? null : empresas.get(0));
        request.setAttribute("filial", logado.getIdFilial() == null
                ? null
                : filialService.buscarPorId(logado.getIdFilial()));

        request.getRequestDispatcher(VIEW_PERFIL).forward(request, response);
    }

    /**
     * Atualiza os dados pessoais do usuário logado. O id vem da sessão e não do formulário,
     * impedindo que a requisição seja usada para alterar o perfil de outra pessoa.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!"editar".equals(request.getParameter("acao"))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Usuario logado = SessaoUtils.usuarioLogado(request);
        Usuario dados = lerUsuario(request, logado.getId());

        String erro = usuarioService.editarProprioPerfil(
                dados, request.getParameter("senha"), logado);

        if (erro == null) {
            response.sendRedirect(request.getContextPath() + "/perfil?ok=editar");
            return;
        }

        request.setAttribute("erroPerfil", erro);
        mostrarFormulario(request, response, dados);
    }

    /** Abre o formulário de usuário no modo de edição do próprio perfil. */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Usuario item)
            throws ServletException, IOException {

        request.setAttribute("modo", "editar");
        request.setAttribute("proprioPerfil", true);
        request.setAttribute("item", item);

        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    /** Lê os dados pessoais do formulário sem permitir alterações de perfil, empresa ou filial. */
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
