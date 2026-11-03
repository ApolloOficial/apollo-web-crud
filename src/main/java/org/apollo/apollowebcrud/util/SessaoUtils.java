package org.apollo.apollowebcrud.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apollo.apollowebcrud.model.Usuario;

/** Acesso ao usuário que está na sessão HTTP. */
public final class SessaoUtils {

    /** Nome do atributo da sessão onde o usuário autenticado fica guardado. */
    public static final String USUARIO_LOGADO = "usuarioLogado";

    /** Impede a criação de instâncias, já que a classe só tem métodos estáticos. */
    private SessaoUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /** Devolve o usuário da sessão atual ou null se não houver sessão. Não cria uma sessão nova. */
    public static Usuario usuarioLogado(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        return sessao == null ? null : (Usuario) sessao.getAttribute(USUARIO_LOGADO);
    }
}
