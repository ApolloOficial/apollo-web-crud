package org.apollo.apollowebcrud.util;

import java.util.regex.Pattern;

public final class EmailUtils {

    // algo@dominio.ext (o banco exige só o "@"; aqui pedimos um formato mínimo de email)
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private EmailUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    public static boolean isEmailValido(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }
}
