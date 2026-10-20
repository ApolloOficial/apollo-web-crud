package org.apollo.apollowebcrud.util;

import org.apollo.apollowebcrud.dao.TipoUsuarioDAO;

public class TipoUsuarioUtils {
    public static final TipoUsuarioDAO tipoUsuarioDAO = new TipoUsuarioDAO();

    public static boolean verificarTipoUsuarioExist(int idTipoUsuario) {
        return tipoUsuarioDAO.buscarPorId(idTipoUsuario) != null;
    }
}
