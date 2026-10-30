package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.SegmentoDAO;
import org.apollo.apollowebcrud.model.Segmento;

import java.util.List;

public class SegmentoService {

    private final SegmentoDAO segmentoDAO = new SegmentoDAO();

    public Segmento buscarPorId(int idSegmento) {
        return idSegmento <= 0 ? null : segmentoDAO.buscarPorId(idSegmento);
    }

    public boolean verificarSegmentoExist(int idSegmento) {
        return segmentoDAO.buscarPorId(idSegmento) != null;
    }

    public List<Segmento> listarTodos() {
        return segmentoDAO.listarTodos();
    }
}
