package org.apollo.apollowebcrud.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Base de todos os DAOs, responsável por abrir a conexão com o PostgreSQL. Os dados de acesso
 * (DATABASE_URL, USER_DB e PASSWORD_DB) são lidos das variáveis de ambiente e, se não existirem,
 * do arquivo .env em src/main/resources.
 */
public abstract class ApolloConnection {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static final String DIRETORIO_ENV =
            System.getProperty("user.dir", ".") + "/src/main/resources";

    private static final Dotenv DOTENV = Dotenv.configure()
            .directory(DIRETORIO_ENV)
            .ignoreIfMissing()
            .load();

    private static final String DATABASE_URL = carregarUrl();
    private static final String USER_DB = carregar("USER_DB").trim();
    private static final String PASSWORD_DB = carregar("PASSWORD_DB");

    /** Lê a URL do banco e confere se ela usa o prefixo jdbc:postgresql://. */
    private static String carregarUrl() {
        String url = carregar("DATABASE_URL").trim();
        if (!url.startsWith("jdbc:postgresql://")) {
            throw new IllegalStateException(
                    "DATABASE_URL deve começar com jdbc:postgresql:// e conter host, porta e banco.");
        }
        return url;
    }

    /** Procura a chave primeiro nas variáveis de ambiente e depois no .env. Lança erro se não achar. */
    private static String carregar(String chave) {
        String valor = System.getenv(chave);
        if (valor == null || valor.isBlank()) {
            valor = DOTENV.get(chave);
        }
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException(
                    "Configuração ausente: " + chave + ". Defina uma variável de ambiente ou "
                            + "configure .env em " + DIRETORIO_ENV + ".");
        }
        return valor;
    }

    /** Abre uma nova conexão. Quem chama deve fechá-la, normalmente com try-with-resources. */
    protected Connection abrirConexao() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, USER_DB, PASSWORD_DB);
    }

    /** Faz rollback sem deixar uma falha no próprio rollback esconder o erro original. */
    protected void desfazer(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.err.println("Erro ao desfazer transação: " + e.getMessage());
        }
    }
}
