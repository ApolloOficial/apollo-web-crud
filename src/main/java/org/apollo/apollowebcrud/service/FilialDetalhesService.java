package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.InversorDAO;
import org.apollo.apollowebcrud.dao.ResumoFilialDAO;
import org.apollo.apollowebcrud.dao.UsuarioDAO;
import org.apollo.apollowebcrud.model.Inversor;
import org.apollo.apollowebcrud.model.ResumoFilial;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;

import java.util.ArrayList;
import java.util.List;

// Dados extras da tela de detalhes da filial. A autorização (a filial é visível para o usuário?)
// já foi feita por quem chama, com FilialService.buscarVisivel.
public class FilialDetalhesService {

    private final ResumoFilialDAO resumoFilialDAO = new ResumoFilialDAO();
    private final InversorDAO inversorDAO = new InversorDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public ResumoFilial montarResumo(int idFilial) {
        ResumoFilial resumo = new ResumoFilial();
        resumo.setTotalInversores(resumoFilialDAO.contarInversores(idFilial));
        resumo.setPotenciaTotalKw(resumoFilialDAO.somarPotenciaKw(idFilial));
        resumo.setTotalStrings(resumoFilialDAO.contarStrings(idFilial));
        resumo.setTotalPaineis(resumoFilialDAO.contarPaineis(idFilial));
        resumo.setPaineisInativos(resumoFilialDAO.contarPaineisInativos(idFilial));
        resumo.setManutencoesEmAberto(resumoFilialDAO.contarManutencoesEmAberto(idFilial));
        resumo.setManutencoesEmAndamento(resumoFilialDAO.contarManutencoesEmAndamento(idFilial));
        return resumo;
    }

    public List<Inversor> listarInversores(int idFilial) {
        return inversorDAO.listarPorFilial(idFilial);
    }

    // Técnicos e operadores da filial; o perfil de cada um já vai preenchido (a view usa ${u.perfil})
    public List<Usuario> listarEquipe(int idFilial) {
        List<Usuario> equipe = new ArrayList<>();
        equipe.addAll(comPerfil(usuarioDAO.listarPorFilialETipo(idFilial, TipoUsuEnum.TECNICO.name()), TipoUsuEnum.TECNICO));
        equipe.addAll(comPerfil(usuarioDAO.listarPorFilialETipo(idFilial, TipoUsuEnum.OPERADOR.name()), TipoUsuEnum.OPERADOR));
        return equipe;
    }

    private List<Usuario> comPerfil(List<Usuario> usuarios, TipoUsuEnum perfil) {
        usuarios.forEach(u -> u.setPerfil(perfil));
        return usuarios;
    }
}
