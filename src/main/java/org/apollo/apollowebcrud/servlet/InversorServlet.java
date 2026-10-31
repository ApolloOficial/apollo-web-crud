package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.model.Inversor;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.FilialService;
import org.apollo.apollowebcrud.service.InversorService;
import org.apollo.apollowebcrud.service.StringSolarService;
import org.apollo.apollowebcrud.util.ParametrosUtils;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;

/**
 * Controle dos inversores da filial do usuário. O GET atende listagem, formulário vazio,
 * formulário preenchido e detalhes. O POST cadastra ou edita conforme o campo acao.
 * A tela de detalhes recebe o inversor e a lista de strings ligadas a ele.
 */
@WebServlet("/inversor")
public class InversorServlet extends HttpServlet {

    private static final String VIEW_LISTA = "/WEB-INF/views/inversor.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/cadastrar-inversor.jsp";
    private static final String VIEW_DETALHES = "/WEB-INF/views/detalhes-inversor.jsp";

    private final InversorService inversorService = new InversorService();
    private final StringSolarService stringSolarService = new StringSolarService();
    private final FilialService filialService = new FilialService();

    /** Escolhe entre listagem, formulário e detalhes de acordo com os parâmetros. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);

        if (request.getParameter("novo") != null) {
            mostrarFormulario(request, response, "novo", null);
        } else if (request.getParameter("editar") != null) {
            Inversor item = inversorService.buscarDaFilial(ParametrosUtils.idOuZero(request.getParameter("editar")), logado);
            if (item == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            mostrarFormulario(request, response, "editar", item);
        } else if (request.getParameter("detalhes") != null) {
            detalhes(request, response, logado);
        } else {
            request.setAttribute("listaInversores", inversorService.listarVisiveis(logado));
            request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
        }
    }

    /** Carrega o inversor, a filial dele e as strings para a tela de detalhes. */
    private void detalhes(HttpServletRequest request, HttpServletResponse response, Usuario logado)
            throws ServletException, IOException {
        int id = ParametrosUtils.idOuZero(request.getParameter("detalhes"));
        Inversor item = inversorService.buscarVisivel(id, logado);
        if (item == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("item", item);
        request.setAttribute("filial", filialService.buscarVisivel(item.getIdFilial(), logado));
        request.setAttribute("listaStrings", stringSolarService.listarDoInversor(id, logado));
        request.getRequestDispatcher(VIEW_DETALHES).forward(request, response);
    }

    /**
     * Valida o formato dos campos e chama o serviço para cadastrar ou editar. Sucesso leva de volta
     * à listagem, erro reabre o formulário com os valores digitados.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);
        boolean editando = "editar".equals(request.getParameter("acao"));

        String erro = erroDeFormato(request);
        Inversor dados = erro == null ? lerInversor(request) : null;
        if (erro == null) {
            erro = editando
                    ? inversorService.editarInversor(dados, logado)
                    : inversorService.cadastrarInversor(dados, logado);
        }

        if (erro == null) {
            response.sendRedirect(request.getContextPath() + "/inversor?ok=" + (editando ? "editar" : "cadastrar"));
            return;
        }

        request.setAttribute("erroInversor", erro);
        mostrarFormulario(request, response, editando ? "editar" : "novo", dados != null ? dados : lerInversor(request));
    }

    /** Encaminha para o formulário com o modo e o inversor a preencher. */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, String modo, Inversor item)
            throws ServletException, IOException {
        request.setAttribute("modo", modo);
        request.setAttribute("item", item);
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    /**
     * Confere potência, data de instalação e status antes de chamar o serviço.
     * Devolve a mensagem do primeiro problema encontrado ou null se estiver tudo certo.
     */
    private String erroDeFormato(HttpServletRequest request) {
        if (invalido(request.getParameter("potenciaKw"), ParametrosUtils.decimalOuNulo(request.getParameter("potenciaKw")))) {
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

    /** Um campo é inválido quando foi preenchido mas não pôde ser convertido. */
    private boolean invalido(String bruto, Object convertido) {
        return bruto != null && !bruto.isBlank() && convertido == null;
    }

    /** Transforma os parâmetros do formulário em um objeto Inversor. */
    private Inversor lerInversor(HttpServletRequest request) {
        Inversor inversor = new Inversor();
        inversor.setId(ParametrosUtils.idOuZero(request.getParameter("id")));
        inversor.setCodigo(request.getParameter("codigo"));
        inversor.setNumeroSerie(request.getParameter("numeroSerie"));
        inversor.setMarca(request.getParameter("marca"));
        inversor.setModelo(request.getParameter("modelo"));
        inversor.setPotenciaKw(ParametrosUtils.decimalOuNulo(request.getParameter("potenciaKw")));
        inversor.setDataInstalacao(ParametrosUtils.dataOuNula(request.getParameter("dataInstalacao")));
        inversor.setStatus(ParametrosUtils.enumOuNulo(StatusGeral.class, request.getParameter("status")));
        return inversor;
    }
}
