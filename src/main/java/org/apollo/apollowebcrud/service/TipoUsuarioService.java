package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.TipoUsuarioDAO;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.TipoUsuario;

/** Converte entre o id dos tipos de usuário no banco e o enum TipoUsuEnum usado na aplicação. */
public class TipoUsuarioService {

    private final TipoUsuarioDAO tipoUsuarioDAO = new TipoUsuarioDAO();

    /** Confere se o tipo existe. */
    public boolean verificarTipoUsuarioExist(int idTipoUsuario) {
        return tipoUsuarioDAO.buscarPorId(idTipoUsuario) != null;
    }

    /** Traduz o id do banco para o perfil, ou null se o tipo não existir. */
    public TipoUsuEnum buscarPerfil(int idTipoUsuario) {
        TipoUsuario tipo = tipoUsuarioDAO.buscarPorId(idTipoUsuario);
        return tipo == null ? null : tipo.getNome();
    }

    /** Faz o caminho inverso e devolve null quando o perfil não está cadastrado. */
    public Integer buscarIdPorPerfil(TipoUsuEnum perfil) {
        TipoUsuario tipo = tipoUsuarioDAO.buscarPorNome(perfil.name());
        return tipo == null ? null : tipo.getId();
    }
}
