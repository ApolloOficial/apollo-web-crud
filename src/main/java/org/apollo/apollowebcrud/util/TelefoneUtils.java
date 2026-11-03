package org.apollo.apollowebcrud.util;

import java.util.regex.Pattern;

/** Validação de telefone brasileiro. */
public final class TelefoneUtils {

    /** Impede a criação de instâncias, já que a classe só tem métodos estáticos. */
    private TelefoneUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /** Aceita (11) 91234-5678, 11912345678, (11) 1234-5678 e 1134567890, com ou sem DDD e nono dígito. */
    public static final Pattern TELEFONE_PATTERN = Pattern.compile("^(\\(?[0-9]{2}\\)? ?)?([0-9]{4}|[0-9]{5})-?[0-9]{4}$");

    /** Aplica a expressão regular e informa se o telefone tem um formato aceito. */
    public static boolean isTelefoneValido(String telefone) {
        if (telefone == null) return false;
        return TELEFONE_PATTERN.matcher(telefone).matches();
    }
}