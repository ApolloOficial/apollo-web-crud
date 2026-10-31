package org.apollo.apollowebcrud.util;

import java.util.regex.Pattern;

/** Validação de CEP. */
public final class CepUtils {

    /** Impede a criação de instâncias, já que a classe só tem métodos estáticos. */
    private CepUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /** Aceita o formato 00000-000 ou 00000000. */
    public static final Pattern CEP_PATTERN = Pattern.compile("^[0-9]{5}-?[0-9]{3}$");

    /** Aplica a expressão regular e informa se o CEP está no formato esperado. */
    public static boolean isCepValido(String cep) {
        if (cep == null) return false;
        return CEP_PATTERN.matcher(cep).matches();
    }
}