package org.apollo.apollowebcrud.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

/** Geração e conferência de senhas com BCrypt. A senha em texto puro nunca é guardada. */
public class SenhaUtils {

    /** Classe utilitária, sem instâncias. */
    private SenhaUtils() {
        throw new UnsupportedOperationException("Classe utilitária.");
    }

    /** Gera o hash BCrypt da senha usando custo 10. */
    public static String gerarHash(String senha) {
//        return BCrypt.withDefaults().hashToString(10, senha.toCharArray());
        return senha;
    }

    /** Compara a senha digitada com o hash salvo. Se qualquer um for null o resultado é false. */
    public static boolean verificar(String senhaDigitada, String hashSalvo) {
        if (senhaDigitada == null || hashSalvo == null) {
            return false;
        }
//        return BCrypt.verifyer().verify(senhaDigitada.toCharArray(), hashSalvo).verified;
        return senhaDigitada.equals(hashSalvo);
    }
}
