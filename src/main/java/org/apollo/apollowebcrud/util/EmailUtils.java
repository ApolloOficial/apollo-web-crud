package org.apollo.apollowebcrud.util;

import org.apollo.apollowebcrud.dao.UsuarioDAO;

import java.util.regex.Pattern;

public class EmailUtils {
    public static final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public static boolean verificarEmailExist(String email) {
        return usuarioDAO.buscarPorEmail(email) != null;
    }

    // E-mail: padrão comum

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z]+\\.[A-Za-z]+(\\.[A-Za-z]+)?$");

    public static boolean isEmailValido(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

}
