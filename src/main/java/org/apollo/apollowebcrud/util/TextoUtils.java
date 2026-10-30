package org.apollo.apollowebcrud.util;

public final class TextoUtils {

    private TextoUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    public static boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    // Texto sem espaços nas pontas, ou null se estiver vazio
    public static String vazioParaNulo(String valor) {
        return vazio(valor) ? null : valor.trim();
    }

    // true se passa do tamanho da coluna do banco
    public static boolean excede(String valor, int maximo) {
        return valor != null && valor.trim().length() > maximo;
    }
}
