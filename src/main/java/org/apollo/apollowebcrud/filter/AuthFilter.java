package org.apollo.apollowebcrud.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.FalhaRevalidacaoSessaoException;
import org.apollo.apollowebcrud.service.UsuarioService;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private final UsuarioService usuarioService = new UsuarioService();
    private final Logger logger = Logger.getLogger(AuthFilter.class.getName());

    private static final Set<String> ROTAS_PUBLICAS = Set.of("/", "/index.jsp", "/login", "/recuperar-senha", "/landing.html", "/landing.css", "/landing.js");
    private static final Set<String> PREFIXOS_PUBLICOS = Set.of("/docs/");

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String caminho = caminho(request);

        if (rotaPublica(caminho)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession sessao = request.getSession(false);
        Object principal = sessao == null ? null : sessao.getAttribute(SessaoUtils.USUARIO_LOGADO);
        if (!(principal instanceof Usuario)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuarioSessao = (Usuario) principal;
        Usuario usuario;
        try {
            usuario = usuarioService.revalidarSessao(usuarioSessao.getId());
        } catch (FalhaRevalidacaoSessaoException e) {
            logger.log(Level.SEVERE, "Falha ao revalidar a sessão; requisição negada temporariamente.", e);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return;
        }

        if (usuario == null) {
            invalidarSessao(sessao);
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            sessao.setAttribute(SessaoUtils.USUARIO_LOGADO, usuario);
        } catch (IllegalStateException e) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (Autorizacao.permite(usuario, caminho) && (!requisicaoDeEscrita(request) || Autorizacao.permiteEscrita(usuario, caminho))) {
            chain.doFilter(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private void invalidarSessao(HttpSession sessao) {
        try {
            sessao.invalidate();
        } catch (IllegalStateException ignorado) {
            // Outra requisição pode ter encerrado a sessão em paralelo.
        }
    }

    // POST, ou GET que abre formulário de cadastro/edição (?novo / ?editar)
    private boolean requisicaoDeEscrita(HttpServletRequest request) {
        return !"GET".equals(request.getMethod())
                || request.getParameter("novo") != null
                || request.getParameter("editar") != null;
    }

    private String caminho(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }

    private boolean rotaPublica(String caminho) {
        return ROTAS_PUBLICAS.contains(caminho)
                || PREFIXOS_PUBLICOS.stream().anyMatch(caminho::startsWith);
    }
}
