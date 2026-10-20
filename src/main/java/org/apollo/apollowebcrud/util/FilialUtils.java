package org.apollo.apollowebcrud.util;

import org.apollo.apollowebcrud.dao.FilialDAO;

public class FilialUtils {
    public static final FilialDAO filialDAO = new FilialDAO();

    public static boolean verificarFilialExist(int idFilial) {
        return filialDAO.buscarPorId(idFilial) != null;
    }
}
