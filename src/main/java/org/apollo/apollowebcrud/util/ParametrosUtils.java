package org.apollo.apollowebcrud.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Conversão dos parâmetros de formulário, que chegam como texto, para os tipos usados no código. */
public final class ParametrosUtils {

    /** Impede a criação de instâncias, já que a classe só tem métodos estáticos. */
    private ParametrosUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /** Converte para número inteiro, devolvendo null se estiver vazio ou inválido. */
    public static Integer inteiroOuNulo(String valor) {
        try {
            return valor == null ? null : Integer.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Igual ao anterior, mas devolve 0 (um id que nunca existe) quando o texto não é um número. */
    public static int idOuZero(String valor) {
        Integer numero = inteiroOuNulo(valor);
        return numero == null ? 0 : numero;
    }

    /** Converte decimais e aceita vírgula. Devolve null se estiver vazio ou inválido. */
    public static Double decimalOuNulo(String valor) {
        try {
            return valor == null || valor.isBlank() ? null : Double.valueOf(valor.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Lê datas no formato do input type date, como 2026-10-25, ou devolve null se vierem vazias ou inválidas.
     */
    public static LocalDate dataOuNula(String valor) {
        try {
            return valor == null || valor.isBlank() ? null : LocalDate.parse(valor.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Converte para o enum indicado ignorando maiúsculas e minúsculas, ou devolve null se não for possível.
     */
    public static <E extends Enum<E>> E enumOuNulo(Class<E> tipo, String valor) {
        try {
            return valor == null || valor.isBlank() ? null : Enum.valueOf(tipo, valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
