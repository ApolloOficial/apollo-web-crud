package org.apollo.apollowebcrud.util;

/** Validação da chave de acesso da nota fiscal eletrônica. */
public final class NotaFiscalUtils {

    /** Impede a criação de instâncias, já que a classe só tem métodos estáticos. */
    private NotaFiscalUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /**
     * Valida a chave da NF-e, que precisa ter exatamente 44 dígitos. O último é um dígito verificador
     * calculado por módulo 11 com pesos de 2 a 9 em ciclo.
     */
    public static boolean isChaveNfeValida(String chave) {
        if (chave == null) return false;

        // Remove tudo que não for dígito
        String digits = chave.replaceAll("\\D", "");

        // Precisa ter exatamente 44 dígitos
        if (digits.length() != 44) return false;

        int[] nums = new int[44];
        for (int i = 0; i < 44; i++) {
            nums[i] = digits.charAt(i) - '0';
        }

        // --- Cálculo do dígito verificador (44º dígito) ---
        // Pesos ciclam de 2 a 9, da direita pra esquerda sobre os 43 primeiros dígitos
        int soma = 0;
        int peso = 2;
        for (int i = 42; i >= 0; i--) {
            soma += nums[i] * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }

        int resto = soma % 11;
        int dv = (resto == 0 || resto == 1) ? 0 : 11 - resto;

        return dv == nums[43];
    }
}