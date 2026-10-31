package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.PainelDAO;
import org.apollo.apollowebcrud.model.Painel;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.model.StringSolar;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.util.NotaFiscalUtils;

import java.util.List;

import static org.apollo.apollowebcrud.util.TextoUtils.excede;
import static org.apollo.apollowebcrud.util.TextoUtils.vazio;
import static org.apollo.apollowebcrud.util.TextoUtils.vazioParaNulo;

/**
 * Regras dos painéis solares. O acesso é limitado à filial do usuário por meio da string de cada painel.
 */
public class PainelService {

    private final PainelDAO painelDAO = new PainelDAO();
    private final StringSolarService stringSolarService = new StringSolarService();

    /** Lista os painéis da filial do usuário. */
    public List<Painel> listarVisiveis(Usuario logado) {
        if (logado.getIdFilial() == null) {
            return List.of();
        }
        return painelDAO.listarPorFilial(logado.getIdFilial());
    }

    /** Lista os painéis de uma string, mas só se ela pertencer à filial do usuário. */
    public List<Painel> listarDaString(int idString, Usuario logado) {
        if (stringSolarService.buscarVisivel(idString, logado) == null) {
            return List.of();
        }
        return painelDAO.listarPorString(idString);
    }

    /** Painel só é devolvido se a string dele for da filial do usuário. */
    public Painel buscarDaFilial(int idPainel, Usuario logado) {
        if (!logado.podeOperarEquipamentos()) {
            return null;
        }
        Painel painel = painelDAO.buscarPorId(idPainel);
        return painel != null && stringSolarService.buscarDaFilial(painel.getIdString(), logado) != null ? painel : null;
    }

    // ---------- CADASTRO ----------

    /** Valida e cadastra o painel, devolvendo null ou a mensagem de erro. */
    public String cadastrarPainel(Painel novo, Usuario logado) {
        if (!logado.podeOperarEquipamentos()) {
            return "Você não tem permissão para cadastrar painéis";
        }

        StringSolar string = stringSolarService.buscarDaFilial(novo.getIdString(), logado);
        if (string == null) {
            return "A string selecionada não existe na sua filial";
        }

        String erro = validar(novo, 0);
        if (erro != null) {
            return erro;
        }

        normalizar(novo);
        novo.setStatus(StatusGeral.ATIVO);

        return painelDAO.inserir(novo) > 0 ? null : "Erro interno no servidor ao tentar salvar o painel";
    }

    // ---------- EDIÇÃO (a string não muda, mas o status pode mudar aqui) ----------

    /** Valida e atualiza o painel. A string de origem não pode ser trocada. */
    public String editarPainel(Painel dados, Usuario logado) {
        Painel atual = buscarDaFilial(dados.getId(), logado);
        if (atual == null) {
            return "Painel não encontrado ou sem permissão";
        }
        if (dados.getStatus() == null) {
            return "O status não foi informado";
        }

        String erro = validar(dados, atual.getId());
        if (erro != null) {
            return erro;
        }

        normalizar(dados);
        atual.setNumeroSerie(dados.getNumeroSerie());
        atual.setMarca(dados.getMarca());
        atual.setModelo(dados.getModelo());
        atual.setPotenciaW(dados.getPotenciaW());
        atual.setNotaFiscal(dados.getNotaFiscal());
        atual.setDataInstalacao(dados.getDataInstalacao());
        atual.setStatus(dados.getStatus());

        return painelDAO.atualizar(atual) ? null : "Erro interno no servidor ao tentar salvar o painel";
    }

    /**
     * Confere o número de série, os tamanhos dos textos, a potência e a chave da nota fiscal (44 dígitos).
     * O número de série também não pode se repetir, exceto no próprio painel durante a edição.
     */
    private String validar(Painel p, int idIgnorar) {
        if (vazio(p.getNumeroSerie())) return "O número de série não foi informado";
        if (excede(p.getNumeroSerie(), 100)) return "O número de série deve ter até 100 caracteres";
        if (excede(p.getMarca(), 60)) return "A marca deve ter até 60 caracteres";
        if (excede(p.getModelo(), 150)) return "O modelo deve ter até 150 caracteres";
        if (p.getPotenciaW() != null && (p.getPotenciaW() <= 0 || p.getPotenciaW() >= 100000)) {
            return "A potência deve ser maior que zero";
        }
        if (!vazio(p.getNotaFiscal()) && !NotaFiscalUtils.isChaveNfeValida(p.getNotaFiscal())) {
            return "A chave da nota fiscal não é válida (44 dígitos)";
        }

        Painel mesmaSerie = painelDAO.buscarPorNumeroSerie(p.getNumeroSerie().trim());
        if (mesmaSerie != null && mesmaSerie.getId() != idIgnorar) {
            return "Este número de série já foi cadastrado no sistema";
        }
        return null;
    }

    /**
     * Remove espaços das pontas, deixa a nota fiscal só com dígitos e transforma textos vazios em null.
     */
    private void normalizar(Painel p) {
        p.setNumeroSerie(p.getNumeroSerie().trim());
        p.setMarca(vazioParaNulo(p.getMarca()));
        p.setModelo(vazioParaNulo(p.getModelo()));
        p.setNotaFiscal(vazio(p.getNotaFiscal()) ? null : p.getNotaFiscal().replaceAll("\\D", ""));
    }
}
