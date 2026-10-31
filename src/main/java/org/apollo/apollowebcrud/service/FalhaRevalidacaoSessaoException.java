package org.apollo.apollowebcrud.service;

/**
 * Sinaliza que não foi possível consultar o banco para revalidar a sessão. O AuthFilter converte isso em erro 503.
 */
public class FalhaRevalidacaoSessaoException extends RuntimeException {

    public FalhaRevalidacaoSessaoException(Throwable causa) {
        super("Não foi possível revalidar a sessão do usuário", causa);
    }
}
