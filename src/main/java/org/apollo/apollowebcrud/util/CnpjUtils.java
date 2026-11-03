package org.apollo.apollowebcrud.util;

/** Funções de apoio para CNPJ, como remoção de máscara e validação dos dígitos. */
public final class CnpjUtils {

    /** Impede a criação de instâncias, já que a classe só tem métodos estáticos. */
    private CnpjUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /** Deixa só os dígitos do CNPJ e devolve texto vazio se ele for null. */
    public static String removerMascara(String cnpj) {
        return cnpj == null ? "" : cnpj.replaceAll("\\D", "");
    }

    /**
     * Valida o CNPJ de verdade, conferindo o tamanho, os dígitos verificadores e rejeitando sequências
     * repetidas como 00.000.000/0000-00, que passam no formato mas não existem.
     */
    public static boolean isCnpjValido(String cnpj) {
        if (cnpj == null) return false;

        // Remove tudo que não for dígito
        String digits = cnpj.replaceAll("\\D", "");

        // Precisa ter exatamente 14 dígitos
        if (digits.length() != 14) return false;

        // Rejeita sequências tipo 00000000000000, 11111111111111 ...
        if (digits.matches("(\\d)\\1{13}")) return false;

        int[] nums = new int[14];
        for (int i = 0; i < 14; i++) {
            nums[i] = digits.charAt(i) - '0';
        }

        // --- Cálculo do 1º dígito verificador ---
        // Pesos: 5,4,3,2,9,8,7,6,5,4,3,2
        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma1 = 0;
        for (int i = 0; i < 12; i++) {
            soma1 += nums[i] * pesos1[i];
        }
        int resto1 = soma1 % 11;
        int dv1 = (resto1 < 2) ? 0 : 11 - resto1;

        if (dv1 != nums[12]) return false;

        // --- Cálculo do 2º dígito verificador ---
        // Pesos: 6,5,4,3,2,9,8,7,6,5,4,3,2
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma2 = 0;
        for (int i = 0; i < 13; i++) {
            soma2 += nums[i] * pesos2[i];
        }
        int resto2 = soma2 % 11;
        int dv2 = (resto2 < 2) ? 0 : 11 - resto2;

        return dv2 == nums[13];
    }
}