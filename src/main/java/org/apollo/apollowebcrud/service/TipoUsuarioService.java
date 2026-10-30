package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.TipoUsuarioDAO;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.TipoUsuario;

public class TipoUsuarioService {

    private final TipoUsuarioDAO tipoUsuarioDAO = new TipoUsuarioDAO();

    public boolean verificarTipoUsuarioExist(int idTipoUsuario) {
        return tipoUsuarioDAO.buscarPorId(idTipoUsuario) != null;
    }

    // Traduz o id do banco para o perfil (null se o tipo não existir)
    public TipoUsuEnum buscarPerfil(int idTipoUsuario) {
        TipoUsuario tipo = tipoUsuarioDAO.buscarPorId(idTipoUsuario);
        return tipo == null ? null : tipo.getNome();
    }

    // Traduz o perfil para o id do banco (null se o tipo não existir)
    public Integer buscarIdPorPerfil(TipoUsuEnum perfil) {
        TipoUsuario tipo = tipoUsuarioDAO.buscarPorNome(perfil.name());
        return tipo == null ? null : tipo.getId();
    }
}
