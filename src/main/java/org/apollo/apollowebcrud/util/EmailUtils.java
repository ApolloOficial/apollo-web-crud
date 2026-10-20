package org.apollo.apollowebcrud.util;

import org.apollo.apollowebcrud.dao.UsuarioDAO;

public class EmailUtils {
    public static final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public static boolean verificarEmailExist(String email) {
        return usuarioDAO.buscarPorEmail(email) != null;
    }
}
