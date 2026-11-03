package org.apollo.apollowebcrud.util;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Leitura de configurações sensíveis, como credenciais de e-mail. A busca começa nas variáveis de
 * ambiente e, se a chave não existir, continua no arquivo .env de src/main/resources, o mesmo usado
 * pela conexão com o banco.
 */
public final class ConfigUtils {

    private static final Dotenv DOTENV = Dotenv.configure()
            .directory(System.getProperty("user.dir", ".") + "/src/main/resources")
            .ignoreIfMissing()
            .load();

    private ConfigUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /** Devolve o valor da chave ou null se ela não estiver definida em nenhum dos dois lugares. */
    public static String valor(String chave) {
        String valor = System.getenv(chave);
        if (valor == null || valor.isBlank()) {
            valor = DOTENV.get(chave);
        }
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    /** Igual ao método anterior, mas devolve o padrão quando a chave não existe. */
    public static String valor(String chave, String padrao) {
        String valor = valor(chave);
        return valor == null ? padrao : valor;
    }
}
