package org.apollo.apollowebcrud.util;

import java.util.regex.Pattern;

public final class TelefoneUtils {

    private TelefoneUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    //    REGEX: aceita (11) 91234-5678, 11912345678, (11) 1234-5678, 1134567890, com/sem DDD e 9º dígito
    public static final Pattern TELEFONE_PATTERN = Pattern.compile("^(\\(?[0-9]{2}\\)? ?)?([0-9]{4}|[0-9]{5})-?[0-9]{4}$");

    //    Aplica o REGEX e verifica se atende ao formato esperado
    public static boolean isTelefoneValido(String telefone) {
        if (telefone == null) return false;
        return TELEFONE_PATTERN.matcher(telefone).matches();
    }
}