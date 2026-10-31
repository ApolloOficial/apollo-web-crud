package org.apollo.apollowebcrud.util;

import java.util.regex.Pattern;

/** Validação simples de e-mail. */
public final class EmailUtils {

    /**
     * Exige o formato algo@dominio.ext. O banco só obriga o @, mas aqui o formato é um pouco mais rígido.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    /** Impede a criação de instâncias, já que a classe só tem métodos estáticos. */
    private EmailUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /** Verdadeiro se o texto, sem espaços nas pontas, segue o formato esperado. */
    public static boolean isEmailValido(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }
}
