package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.model.Manutencao;
import org.apollo.apollowebcrud.model.Painel;
import org.apollo.apollowebcrud.model.StatusManutencao;
import org.apollo.apollowebcrud.model.TipoManutencao;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.ManutencaoService;
import org.apollo.apollowebcrud.service.PainelService;
import org.apollo.apollowebcrud.service.UsuarioService;
import org.apollo.apollowebcrud.util.ParametrosUtils;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controle das manutenções dos painéis da filial. O GET atende listagem, formulário vazio e
 * formulário preenchido, e o POST cadastra ou edita de acordo com o campo acao.
 * O formulário recebe modo, item, listaPaineis e listaResponsaveis para os campos de escolha.
 */
@WebServlet("/manutencao")
public class ManutencaoServlet extends HttpServlet {

    private static final String VIEW_LISTA = "/WEB-INF/views/manutencao.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/cadastrar-manutencao.jsp";

    private final ManutencaoService manutencaoService = new ManutencaoService();
    private final PainelService painelService = new PainelService();
    private final UsuarioService usuarioService = new UsuarioService();

    /** Abre a listagem ou o formulário, conforme os parâmetros novo e editar. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);

        if (request.getParameter("novo") != null) {
            mostrarFormulario(request, response, "novo", null);
        } else if (request.getParameter("editar") != null) {
            Manutencao item = manutencaoService.buscarDaFilial(ParametrosUtils.idOuZero(request.getParameter("editar")), logado);
            if (item == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            mostrarFormulario(request, response, "editar", item);
        } else {
            request.setAttribute("listaManutencoes", manutencaoService.listarVisiveis(logado));
            request.setAttribute("seriesPaineis", seriesDosPaineis(logado));
            request.setAttribute("nomesResponsaveis", nomesDosResponsaveis(logado));
            request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
        }
    }

    /**
     * Valida o formato dos campos e delega ao serviço o cadastro ou a edição. O painel não muda
     * durante a edição, então o original é preservado se houver erro.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);
        boolean editando = "editar".equals(request.getParameter("acao"));

        String erro = erroDeFormato(request, editando);
        Manutencao dados = lerManutencao(request);
        if (erro == null) {
            erro = editando
                    ? manutencaoService.editarManutencao(dados, logado)
                    : manutencaoService.cadastrarManutencao(dados, logado);
        }

        if (erro == null) {
            response.sendRedirect(request.getContextPath() + "/manutencao?ok=" + (editando ? "editar" : "cadastrar"));
            return;
        }

        request.setAttribute("erroManutencao", erro);
        if (editando) {
            manterPainelOriginal(dados, logado); // o painel não muda na edição
        }
        mostrarFormulario(request, response, editando ? "editar" : "novo", dados);
    }

    /** Carrega os painéis e os responsáveis possíveis da filial e abre o formulário. */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, String modo, Manutencao item)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);
        request.setAttribute("modo", modo);
        request.setAttribute("item", item);
        request.setAttribute("listaPaineis", painelService.listarVisiveis(logado));
        request.setAttribute("listaResponsaveis", usuarioService.listarResponsaveisDaFilial(logado));
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    /** Mapa de id do painel para número de série, usado nos campos de escolha. */
    private Map<Integer, String> seriesDosPaineis(Usuario logado) {
        Map<Integer, String> series = new HashMap<>();
        for (Painel painel : painelService.listarVisiveis(logado)) {
            series.put(painel.getId(), painel.getNumeroSerie());
        }
        return series;
    }

    /** Mapa de id do usuário para nome, com os responsáveis que podem receber a manutenção. */
    private Map<Integer, String> nomesDosResponsaveis(Usuario logado) {
        Map<Integer, String> nomes = new HashMap<>();
        for (Usuario responsavel : usuarioService.listarResponsaveisDaFilial(logado)) {
            nomes.put(responsavel.getId(), responsavel.getNome());
        }
        return nomes;
    }

    /** Recoloca o painel gravado no banco, já que ele não pode ser trocado na edição. */
    private void manterPainelOriginal(Manutencao dados, Usuario logado) {
        Manutencao original = manutencaoService.buscarDaFilial(dados.getId(), logado);
        if (original != null) {
            dados.setIdPainel(original.getIdPainel());
        }
    }

    /**
     * Verifica tipo, status (apenas na edição) e as três datas da manutenção.
     * Devolve a mensagem de erro ou null quando tudo está válido.
     */
    private String erroDeFormato(HttpServletRequest request, boolean editando) {
        if (ParametrosUtils.enumOuNulo(TipoManutencao.class, request.getParameter("tipoManutencao")) == null) {
            return "O tipo da manutenção não é válido";
        }
        if (editando && ParametrosUtils.enumOuNulo(StatusManutencao.class, request.getParameter("status")) == null) {
            return "O status informado não é válido";
        }
        for (String campo : new String[]{"dataAbertura", "dataPrevista", "dataConclusao"}) {
            String bruto = request.getParameter(campo);
            if (bruto != null && !bruto.isBlank() && ParametrosUtils.dataOuNula(bruto) == null) {
                return "A data informada em " + campo + " não é válida";
            }
        }
        return null;
    }

    /** Constrói a Manutencao a partir dos parâmetros do formulário. */
    private Manutencao lerManutencao(HttpServletRequest request) {
        Manutencao m = new Manutencao();
        m.setId(ParametrosUtils.idOuZero(request.getParameter("id")));
        m.setIdPainel(ParametrosUtils.idOuZero(request.getParameter("painel")));
        m.setTipoManutencao(ParametrosUtils.enumOuNulo(TipoManutencao.class, request.getParameter("tipoManutencao")));
        m.setDescricao(request.getParameter("descricao"));
        m.setStatus(ParametrosUtils.enumOuNulo(StatusManutencao.class, request.getParameter("status")));
        m.setDataAbertura(ParametrosUtils.dataOuNula(request.getParameter("dataAbertura")));
        m.setDataPrevista(ParametrosUtils.dataOuNula(request.getParameter("dataPrevista")));
        m.setDataConclusao(ParametrosUtils.dataOuNula(request.getParameter("dataConclusao")));
        m.setIdUsuarioResponsavel(ParametrosUtils.inteiroOuNulo(request.getParameter("responsavel")));
        return m;
    }
}
