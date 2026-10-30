package org.apollo.apollowebcrud.service;

public class FalhaRevalidacaoSessaoException extends RuntimeException {

    public FalhaRevalidacaoSessaoException(Throwable causa) {
        super("Não foi possível revalidar a sessão do usuário", causa);
    }
}
