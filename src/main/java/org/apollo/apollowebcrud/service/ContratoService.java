package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.ContratoDAO;
import org.apollo.apollowebcrud.model.Contrato;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;

public class ContratoService {

    private final ContratoDAO contratoDAO = new ContratoDAO();

    // Contrato é somente leitura e cada gerente consulta o único contrato da própria empresa.
    public Contrato buscarDaEmpresa(Usuario gerente) {
        if (gerente == null || gerente.getPerfil() != TipoUsuEnum.GERENTE || gerente.getIdEmpresa() == null) {
            return null;
        }
        return contratoDAO.buscarPorEmpresa(gerente.getIdEmpresa());
    }
}
