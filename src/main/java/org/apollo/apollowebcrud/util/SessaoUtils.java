package org.apollo.apollowebcrud.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apollo.apollowebcrud.model.Usuario;

public final class SessaoUtils {

    public static final String USUARIO_LOGADO = "usuarioLogado";

    private SessaoUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    public static Usuario usuarioLogado(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        return sessao == null ? null : (Usuario) sessao.getAttribute(USUARIO_LOGADO);
    }
}
