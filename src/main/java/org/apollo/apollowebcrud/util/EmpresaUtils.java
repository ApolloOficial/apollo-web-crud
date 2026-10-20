package org.apollo.apollowebcrud.util;

import org.apollo.apollowebcrud.dao.EmpresaDAO;
import org.apollo.apollowebcrud.dao.FilialDAO;

public class EmpresaUtils {
    public static final EmpresaDAO empresaDAO = new EmpresaDAO();

    public static boolean verificarEmpresaExist(int idEmpresa) {
        return empresaDAO.buscarPorId(idEmpresa) != null;
    }
}
