package org.apollo.apollowebcrud.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SenhaUtils {

    private SenhaUtils() {
        throw new UnsupportedOperationException("Classe utilitária.");
    }

    public static String gerarHash(String senha) {
        return BCrypt.withDefaults().hashToString(10, senha.toCharArray());
    }

    public static boolean verificar(String senhaDigitada, String hashSalvo) {
        if (senhaDigitada == null || hashSalvo == null) {
            return false;
        }
        return BCrypt.verifyer().verify(senhaDigitada.toCharArray(), hashSalvo).verified;
    }
}
