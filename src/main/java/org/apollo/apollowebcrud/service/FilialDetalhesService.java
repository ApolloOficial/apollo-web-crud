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

/**
 * Dados complementares da tela de detalhes da filial. Quem chama já confirmou, com
 * FilialService.buscarVisivel, que o usuário tem direito de ver aquela filial.
 */
public class FilialDetalhesService {

    private final ResumoFilialDAO resumoFilialDAO = new ResumoFilialDAO();
    private final InversorDAO inversorDAO = new InversorDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /** Reúne as contagens de inversores, strings, painéis e manutenções em um único objeto. */
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

    /** Lista os inversores da filial. */
    public List<Inversor> listarInversores(int idFilial) {
        return inversorDAO.listarPorFilial(idFilial);
    }

    /** Lista técnicos e operadores da filial já com o perfil preenchido, pois a view usa ${u.perfil}. */
    public List<Usuario> listarEquipe(int idFilial) {
        List<Usuario> equipe = new ArrayList<>();
        equipe.addAll(comPerfil(usuarioDAO.listarPorFilialETipo(idFilial, TipoUsuEnum.TECNICO.name()), TipoUsuEnum.TECNICO));
        equipe.addAll(comPerfil(usuarioDAO.listarPorFilialETipo(idFilial, TipoUsuEnum.OPERADOR.name()), TipoUsuEnum.OPERADOR));
        return equipe;
    }

    /** Preenche o perfil de cada usuário da lista, já que ela contém um único tipo. */
    private List<Usuario> comPerfil(List<Usuario> usuarios, TipoUsuEnum perfil) {
        usuarios.forEach(u -> u.setPerfil(perfil));
        return usuarios;
    }
}
