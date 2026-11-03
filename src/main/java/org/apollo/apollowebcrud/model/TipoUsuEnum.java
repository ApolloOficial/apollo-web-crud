package org.apollo.apollowebcrud.model;

/**
 * Perfis de acesso. O ADMINISTRADOR existe no banco, mas não tem telas nesta aplicação. Gerente, técnico
 * e operador são os perfis atendidos pelo AuthFilter.
 */
public enum TipoUsuEnum {
    ADMINISTRADOR,
    GERENTE,
    TECNICO,
    OPERADOR
}