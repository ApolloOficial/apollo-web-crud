package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.ContratoDAO;
import org.apollo.apollowebcrud.model.Contrato;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;

/** Regras de consulta do contrato. Como o contrato é somente leitura, a classe só expõe a busca. */
public class ContratoService {

    private final ContratoDAO contratoDAO = new ContratoDAO();

    /** Devolve o contrato da empresa do gerente informado, ou null se ele ainda não tem empresa. */
    public Contrato buscarDaEmpresa(Usuario gerente) {
        if (gerente == null || gerente.getPerfil() != TipoUsuEnum.GERENTE || gerente.getIdEmpresa() == null) {
            return null;
        }
        return contratoDAO.buscarPorEmpresa(gerente.getIdEmpresa());
    }
}
