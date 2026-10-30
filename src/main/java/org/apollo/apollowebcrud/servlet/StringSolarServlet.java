package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.model.StringSolar;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.InversorService;
import org.apollo.apollowebcrud.service.PainelService;
import org.apollo.apollowebcrud.service.StringSolarService;
import org.apollo.apollowebcrud.util.ParametrosUtils;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;

// GET /string (listagem) | GET /string?novo=1 | GET /string?editar=ID
// POST com o campo "acao": cadastrar (padrão) | editar
// GET /string?detalhes=ID (tela de detalhes)
// Atributos do formulário: modo ("novo" ou "editar"), item (a StringSolar) e listaInversores (para o select)
// Atributos dos detalhes: item (a StringSolar), inversor (o Inversor dela) e listaPaineis (os painéis dela)
@WebServlet("/string")
public class StringSolarServlet extends HttpServlet {

    private static final String VIEW_LISTA = "/WEB-INF/views/string.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/cadastro-string-interno.jsp";
    private static final String VIEW_DETALHES = "/WEB-INF/views/detalhes-string.jsp";

    private final StringSolarService stringSolarService = new StringSolarService();
    private final InversorService inversorService = new InversorService();
    private final PainelService painelService = new PainelService();

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
            request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
        }
    }

    private void detalhes(HttpServletRequest request, HttpServletResponse response, Usuario logado)
            throws ServletException, IOException {
        int id = ParametrosUtils.idOuZero(request.getParameter("detalhes"));
        StringSolar item = stringSolarService.buscarVisivel(id, logado);
        if (item == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("item", item);
        request.setAttribute("inversor", inversorService.buscarVisivel(item.getIdInversor(), logado));
        request.setAttribute("listaPaineis", painelService.listarDaString(id, logado));
        request.getRequestDispatcher(VIEW_DETALHES).forward(request, response);
    }

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
            response.sendRedirect(request.getContextPath() + "/string");
            return;
        }

        request.setAttribute("erroString", erro);
        if (editando) {
            manterInversorOriginal(dados, logado); // o inversor não muda na edição
        }
        mostrarFormulario(request, response, editando ? "editar" : "novo", dados);
    }

    // Lista de inversores da filial para o campo de escolha do formulário
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, String modo, StringSolar item)
            throws ServletException, IOException {
        request.setAttribute("modo", modo);
        request.setAttribute("item", item);
        request.setAttribute("listaInversores", inversorService.listarVisiveis(SessaoUtils.usuarioLogado(request)));
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private void manterInversorOriginal(StringSolar dados, Usuario logado) {
        StringSolar original = stringSolarService.buscarDaFilial(dados.getId(), logado);
        if (original != null) {
            dados.setIdInversor(original.getIdInversor());
        }
    }

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

    private boolean invalido(String bruto, Object convertido) {
        return bruto != null && !bruto.isBlank() && convertido == null;
    }

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
