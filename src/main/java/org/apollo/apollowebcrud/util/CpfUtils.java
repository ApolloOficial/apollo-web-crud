package org.apollo.apollowebcrud.util;

/** Funções de apoio para CPF, como remoção de máscara e validação dos dígitos. */
public final class CpfUtils {

    /** Impede a criação de instâncias, já que a classe só tem métodos estáticos. */
    private CpfUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /** Deixa só os dígitos do CPF e devolve texto vazio se ele for null. */
    public static String removerMascara(String cpf) {
        return cpf == null ? "" : cpf.trim().replaceAll("[^0-9]", "");
    }

    /** Confere formato e dígitos verificadores, rejeitando sequências repetidas como 111.111.111-11. */
    public static boolean isValido(String cpf) {
        String digitos = removerMascara(cpf);

        if (digitos.length() != 11 || digitos.matches("(\\d)\\1{10}")) {
            return false;
        }

        return digitoVerificador(digitos, 9) == digitos.charAt(9) - '0'
                && digitoVerificador(digitos, 10) == digitos.charAt(10) - '0';
    }

    /** Calcula um dígito verificador a partir dos primeiros dígitos do CPF. */
    private static int digitoVerificador(String digitos, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (digitos.charAt(i) - '0') * (tamanho + 1 - i);
        }
        return (soma * 10 % 11) % 10;
    }
}
