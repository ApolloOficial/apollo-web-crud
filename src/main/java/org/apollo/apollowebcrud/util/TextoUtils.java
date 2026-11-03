package org.apollo.apollowebcrud.util;

/** Pequenos auxiliares para tratar textos vindos dos formulários. */
public final class TextoUtils {

    /** Impede a criação de instâncias, já que a classe só tem métodos estáticos. */
    private TextoUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /** Verdadeiro para texto null ou formado só por espaços. */
    public static boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    /** Remove espaços das pontas e devolve null se o texto ficar vazio. */
    public static String vazioParaNulo(String valor) {
        return vazio(valor) ? null : valor.trim();
    }

    /** Informa se o texto passa do tamanho da coluna no banco. */
    public static boolean excede(String valor, int maximo) {
        return valor != null && valor.trim().length() > maximo;
    }
}
