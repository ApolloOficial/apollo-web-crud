package org.apollo.apollowebcrud.util;

import org.apollo.apollowebcrud.dao.UsuarioDAO;

public final class CpfUtils {
    public static final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private CpfUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    // VERIFICAR UTILIDADE
    public static String removerMascara(String cpf) {
        return cpf == null ? "" : cpf.trim().replaceAll("[^0-9]", "");
    }

    public static boolean verificarCpfExist(String cpf) {
        return usuarioDAO.buscarPorCpf(cpf) != null;
    }
}
