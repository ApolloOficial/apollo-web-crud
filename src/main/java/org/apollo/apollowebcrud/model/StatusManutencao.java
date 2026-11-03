package org.apollo.apollowebcrud.model;

/** Etapas pelas quais uma manutenção passa, da abertura até a conclusão ou o cancelamento. */
public enum StatusManutencao {
    ABERTA,
    EM_ANDAMENTO,
    CONCLUIDA,
    CANCELADA
}
