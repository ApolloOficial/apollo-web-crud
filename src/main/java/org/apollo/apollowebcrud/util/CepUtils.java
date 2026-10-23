package org.apollo.apollowebcrud.util;

import java.util.regex.Pattern;

public final class CepUtils {

    private CepUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    //    REGEX: aceita formato 00000-000 ou 00000000
    public static final Pattern CEP_PATTERN = Pattern.compile("^[0-9]{5}-?[0-9]{3}$");

    //    Aplica o REGEX e verifica se atende ao formato esperado
    public static boolean isCepValido(String cep) {
        if (cep == null) return false;
        return CEP_PATTERN.matcher(cep).matches();
    }
}