package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.model.Painel;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.model.StringSolar;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.PainelService;
import org.apollo.apollowebcrud.service.StringSolarService;
import org.apollo.apollowebcrud.util.ParametrosUtils;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controle dos painéis solares da filial. O GET atende listagem, formulário vazio e preenchido,
 * e o POST cadastra ou edita conforme o campo acao. O formulário recebe modo, item e
 * listaStrings, usada no campo de escolha da string.
 */
@WebServlet("/painel")
public class PainelServlet extends HttpServlet {

    private static final String VIEW_LISTA = "/WEB-INF/views/painel.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/cadastrar-painel.jsp";

    private final PainelService painelService = new PainelService();
    private final StringSolarService stringSolarService = new StringSolarService();

    /** Abre a listagem ou o formulário, conforme os parâmetros novo e editar. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);

        if (request.getParameter("novo") != null) {
            mostrarFormulario(request, response, "novo", null);
        } else if (request.getParameter("editar") != null) {
            Painel item = painelService.buscarDaFilial(ParametrosUtils.idOuZero(request.getParameter("editar")), logado);
            if (item == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            mostrarFormulario(request, response, "editar", item);
        } else {
            request.setAttribute("listaPaineis", painelService.listarVisiveis(logado));
            request.setAttribute("codigosString", codigosDasStrings(logado));
            request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
        }
    }

    /**
     * Valida o formato, chama o serviço e redireciona para a listagem se tudo correr bem.
     * Na falha o formulário volta com o que foi digitado e a mensagem de erro.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);
        boolean editando = "editar".equals(request.getParameter("acao"));

        String erro = erroDeFormato(request);
        Painel dados = lerPainel(request);
        if (erro == null) {
            erro = editando
                    ? painelService.editarPainel(dados, logado)
                    : painelService.cadastrarPainel(dados, logado);
        }

        if (erro == null) {
            response.sendRedirect(request.getContextPath() + "/painel?ok=" + (editando ? "editar" : "cadastrar"));
            return;
        }

        request.setAttribute("erroPainel", erro);
        if (editando) {
            manterStringOriginal(dados, logado); // a string não muda na edição
        }
        mostrarFormulario(request, response, editando ? "editar" : "novo", dados);
    }

    /** Carrega as strings da filial para o campo de escolha e abre o formulário. */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, String modo, Painel item)
            throws ServletException, IOException {
        request.setAttribute("modo", modo);
        request.setAttribute("item", item);
        request.setAttribute("listaStrings", stringSolarService.listarVisiveis(SessaoUtils.usuarioLogado(request)));
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    /** Mapa de id da string para o código dela. */
    private Map<Integer, String> codigosDasStrings(Usuario logado) {
        Map<Integer, String> codigos = new HashMap<>();
        for (StringSolar string : stringSolarService.listarVisiveis(logado)) {
            codigos.put(string.getId(), string.getCodigo());
        }
        return codigos;
    }

    /** Mantém a string já gravada no painel, pois ela não muda na edição. */
    private void manterStringOriginal(Painel dados, Usuario logado) {
        Painel original = painelService.buscarDaFilial(dados.getId(), logado);
        if (original != null) {
            dados.setIdString(original.getIdString());
        }
    }

    /** Confere potência, data de instalação e status, devolvendo o erro encontrado ou null. */
    private String erroDeFormato(HttpServletRequest request) {
        if (invalido(request.getParameter("potenciaW"), ParametrosUtils.decimalOuNulo(request.getParameter("potenciaW")))) {
            return "A potência informada não é um número válido";
        }
        if (invalido(request.getParameter("dataInstalacao"), ParametrosUtils.dataOuNula(request.getParameter("dataInstalacao")))) {
            return "A data de instalação não é válida";
        }
        if ("editar".equals(request.getParameter("acao"))
                && ParametrosUtils.enumOuNulo(StatusGeral.class, request.getParameter("status")) == null) {
            return "O status informado não é válido";
        }
        return null;
    }

    /** Campo preenchido que não pôde ser convertido é tratado como inválido. */
    private boolean invalido(String bruto, Object convertido) {
        return bruto != null && !bruto.isBlank() && convertido == null;
    }

    /** Lê o formulário e devolve um Painel. */
    private Painel lerPainel(HttpServletRequest request) {
        Painel painel = new Painel();
        painel.setId(ParametrosUtils.idOuZero(request.getParameter("id")));
        painel.setIdString(ParametrosUtils.idOuZero(request.getParameter("string")));
        painel.setNumeroSerie(request.getParameter("numeroSerie"));
        painel.setMarca(request.getParameter("marca"));
        painel.setModelo(request.getParameter("modelo"));
        painel.setPotenciaW(ParametrosUtils.decimalOuNulo(request.getParameter("potenciaW")));
        painel.setNotaFiscal(request.getParameter("notaFiscal"));
        painel.setDataInstalacao(ParametrosUtils.dataOuNula(request.getParameter("dataInstalacao")));
        painel.setStatus(ParametrosUtils.enumOuNulo(StatusGeral.class, request.getParameter("status")));
        return painel;
    }
}
