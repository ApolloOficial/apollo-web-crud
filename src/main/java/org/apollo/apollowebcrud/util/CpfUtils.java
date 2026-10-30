package org.apollo.apollowebcrud.util;

public final class CpfUtils {

    private CpfUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    public static String removerMascara(String cpf) {
        return cpf == null ? "" : cpf.trim().replaceAll("[^0-9]", "");
    }

    // Formato + dígitos verificadores; rejeita sequências repetidas (111.111.111-11)
    public static boolean isValido(String cpf) {
        String digitos = removerMascara(cpf);

        if (digitos.length() != 11 || digitos.matches("(\\d)\\1{10}")) {
            return false;
        }

        return digitoVerificador(digitos, 9) == digitos.charAt(9) - '0'
                && digitoVerificador(digitos, 10) == digitos.charAt(10) - '0';
    }

    private static int digitoVerificador(String digitos, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (digitos.charAt(i) - '0') * (tamanho + 1 - i);
        }
        return (soma * 10 % 11) % 10;
    }
}
