package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.model.Inversor;
import org.apollo.apollowebcrud.model.Manutencao;
import org.apollo.apollowebcrud.model.Painel;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.model.StringSolar;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.FilialService;
import org.apollo.apollowebcrud.service.InversorService;
import org.apollo.apollowebcrud.service.ManutencaoService;
import org.apollo.apollowebcrud.service.PainelService;
import org.apollo.apollowebcrud.service.StringSolarService;
import org.apollo.apollowebcrud.util.ParametrosUtils;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Controle das strings, que agrupam os painéis ligados a um inversor. O GET atende listagem,
 * formulários e detalhes e o POST cadastra ou edita conforme o campo acao.
 * Os detalhes recebem a string, o inversor dela, os painéis, as manutenções e as séries dos painéis.
 */
@WebServlet("/string")
public class StringSolarServlet extends HttpServlet {

    private static final String VIEW_LISTA = "/WEB-INF/views/string.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/cadastro-string-interno.jsp";
    private static final String VIEW_DETALHES = "/WEB-INF/views/detalhes-string.jsp";

    private final StringSolarService stringSolarService = new StringSolarService();
    private final InversorService inversorService = new InversorService();
    private final PainelService painelService = new PainelService();
    private final FilialService filialService = new FilialService();
    private final ManutencaoService manutencaoService = new ManutencaoService();

    /** Direciona para a listagem, o formulário ou os detalhes de acordo com os parâmetros. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);

        if (request.getParameter("novo") != null) {
            mostrarFormulario(request, response, "novo", null);
        } else if (request.getParameter("editar") != null) {
            StringSolar item = stringSolarService.buscarDaFilial(ParametrosUtils.idOuZero(request.getParameter("editar")), logado);
            if (item == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            mostrarFormulario(request, response, "editar", item);
        } else if (request.getParameter("detalhes") != null) {
            detalhes(request, response, logado);
        } else {
            request.setAttribute("listaStrings", stringSolarService.listarVisiveis(logado));
            request.setAttribute("codigosInversor", codigosDosInversores(logado));
            request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
        }
    }

    /** Monta a tela de detalhes com inversor, filial, painéis e manutenções da string. */
    private void detalhes(HttpServletRequest request, HttpServletResponse response, Usuario logado)
            throws ServletException, IOException {
        int id = ParametrosUtils.idOuZero(request.getParameter("detalhes"));
        StringSolar item = stringSolarService.buscarVisivel(id, logado);
        if (item == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("item", item);
        Inversor inversor = inversorService.buscarVisivel(item.getIdInversor(), logado);
        List<Painel> paineis = painelService.listarDaString(id, logado);
        request.setAttribute("inversor", inversor);
        request.setAttribute("filial", inversor == null ? null : filialService.buscarVisivel(inversor.getIdFilial(), logado));
        request.setAttribute("listaPaineis", paineis);
        request.setAttribute("listaManutencoes", manutencoesDosPaineis(paineis, logado));
        request.setAttribute("seriesPaineis", seriesDosPaineis(paineis));
        request.getRequestDispatcher(VIEW_DETALHES).forward(request, response);
    }

    /**
     * Valida os campos e executa o cadastro ou a edição pelo serviço. Redireciona para a listagem
     * em caso de sucesso e reabre o formulário com a mensagem quando há erro.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);
        boolean editando = "editar".equals(request.getParameter("acao"));

        String erro = erroDeFormato(request);
        StringSolar dados = lerString(request);
        if (erro == null) {
            erro = editando
                    ? stringSolarService.editarString(dados, logado)
                    : stringSolarService.cadastrarString(dados, logado);
        }

        if (erro == null) {
            response.sendRedirect(request.getContextPath() + "/string?ok=" + (editando ? "editar" : "cadastrar"));
            return;
        }

        request.setAttribute("erroString", erro);
        if (editando) {
            manterInversorOriginal(dados, logado); // o inversor não muda na edição
        }
        mostrarFormulario(request, response, editando ? "editar" : "novo", dados);
    }

    /** Carrega os inversores da filial para o campo de escolha e abre o formulário. */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, String modo, StringSolar item)
            throws ServletException, IOException {
        request.setAttribute("modo", modo);
        request.setAttribute("item", item);
        request.setAttribute("listaInversores", inversorService.listarVisiveis(SessaoUtils.usuarioLogado(request)));
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    /** Mapa de id do inversor para o código dele. */
    private Map<Integer, String> codigosDosInversores(Usuario logado) {
        Map<Integer, String> codigos = new HashMap<>();
        for (Inversor inversor : inversorService.listarVisiveis(logado)) {
            codigos.put(inversor.getId(), inversor.getCodigo());
        }
        return codigos;
    }

    /** Filtra as manutenções visíveis para o usuário que pertencem aos painéis informados. */
    private List<Manutencao> manutencoesDosPaineis(List<Painel> paineis, Usuario logado) {
        Set<Integer> ids = new HashSet<>();
        for (Painel painel : paineis) {
            ids.add(painel.getId());
        }
        return manutencaoService.listarVisiveis(logado).stream()
                .filter(m -> ids.contains(m.getIdPainel()))
                .toList();
    }

    /** Mapa de id do painel para número de série. */
    private Map<Integer, String> seriesDosPaineis(List<Painel> paineis) {
        Map<Integer, String> series = new HashMap<>();
        for (Painel painel : paineis) {
            series.put(painel.getId(), painel.getNumeroSerie());
        }
        return series;
    }

    /** O inversor da string não muda na edição, então o valor original é recolocado. */
    private void manterInversorOriginal(StringSolar dados, Usuario logado) {
        StringSolar original = stringSolarService.buscarDaFilial(dados.getId(), logado);
        if (original != null) {
            dados.setIdInversor(original.getIdInversor());
        }
    }

    /** Confere MPPT, entrada e status, devolvendo a primeira mensagem de erro ou null. */
    private String erroDeFormato(HttpServletRequest request) {
        if (invalido(request.getParameter("numeroMppt"), ParametrosUtils.inteiroOuNulo(request.getParameter("numeroMppt")))) {
            return "O número MPPT não é válido";
        }
        if (invalido(request.getParameter("numeroEntrada"), ParametrosUtils.inteiroOuNulo(request.getParameter("numeroEntrada")))) {
            return "O número de entrada não é válido";
        }
        if ("editar".equals(request.getParameter("acao"))
                && ParametrosUtils.enumOuNulo(StatusGeral.class, request.getParameter("status")) == null) {
            return "O status informado não é válido";
        }
        return null;
    }

    /** Verdadeiro quando o campo veio preenchido mas a conversão falhou. */
    private boolean invalido(String bruto, Object convertido) {
        return bruto != null && !bruto.isBlank() && convertido == null;
    }

    /** Converte os parâmetros do formulário em uma StringSolar. */
    private StringSolar lerString(HttpServletRequest request) {
        StringSolar string = new StringSolar();
        string.setId(ParametrosUtils.idOuZero(request.getParameter("id")));
        string.setIdInversor(ParametrosUtils.idOuZero(request.getParameter("inversor")));
        string.setCodigo(request.getParameter("codigo"));
        string.setNumeroMppt(ParametrosUtils.inteiroOuNulo(request.getParameter("numeroMppt")));
        string.setNumeroEntrada(ParametrosUtils.inteiroOuNulo(request.getParameter("numeroEntrada")));
        string.setStatus(ParametrosUtils.enumOuNulo(StatusGeral.class, request.getParameter("status")));
        return string;
    }
}
