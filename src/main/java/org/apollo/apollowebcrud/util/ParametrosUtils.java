package org.apollo.apollowebcrud.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class ParametrosUtils {

    private ParametrosUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    // Número do formulário, ou null se vier vazio/inválido
    public static Integer inteiroOuNulo(String valor) {
        try {
            return valor == null ? null : Integer.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Igual ao anterior, mas devolve 0 (id que nunca existe) quando não é número
    public static int idOuZero(String valor) {
        Integer numero = inteiroOuNulo(valor);
        return numero == null ? 0 : numero;
    }

    // Decimal do formulário (aceita vírgula), ou null se vier vazio/inválido
    public static Double decimalOuNulo(String valor) {
        try {
            return valor == null || valor.isBlank() ? null : Double.valueOf(valor.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Data no formato do <input type="date"> (2026-10-25), ou null se vier vazia/inválida
    public static LocalDate dataOuNula(String valor) {
        try {
            return valor == null || valor.isBlank() ? null : LocalDate.parse(valor.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // Valor de um enum (ignora maiúsculas), ou null se vier vazio/inválido
    public static <E extends Enum<E>> E enumOuNulo(Class<E> tipo, String valor) {
        try {
            return valor == null || valor.isBlank() ? null : Enum.valueOf(tipo, valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
