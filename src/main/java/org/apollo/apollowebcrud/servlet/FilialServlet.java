package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.model.Endereco;
import org.apollo.apollowebcrud.model.Filial;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.FilialDetalhesService;
import org.apollo.apollowebcrud.service.FilialService;
import org.apollo.apollowebcrud.service.SegmentoService;
import org.apollo.apollowebcrud.util.ParametrosUtils;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;

// GET /filial (listagem) | ?novo=1 (formulário vazio) | ?editar=ID (formulário preenchido) | ?detalhes=ID (tela de detalhes)
// POST com o campo "acao": cadastrar (padrão) | editar | inativar | reativar
// Formulário: modo ("novo"/"editar"), item (Filial), endereco (Endereco) e listaSegmentos (select)
// Detalhes: item (Filial), endereco (Endereco), segmento (Segmento), resumo (ResumoFilial, as métricas),
// listaInversores (inversores da filial) e equipe (técnicos e operadores)
@WebServlet("/filial")
public class FilialServlet extends HttpServlet {

    private static final String VIEW_LISTA = "/WEB-INF/views/filial.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/Cadastrar-filial.jsp";
    private static final String VIEW_DETALHES = "/WEB-INF/views/detalhes-filial.jsp";

    private final FilialService filialService = new FilialService();
    private final SegmentoService segmentoService = new SegmentoService();
    private final FilialDetalhesService filialDetalhesService = new FilialDetalhesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);

        if (request.getParameter("novo") != null) {
            mostrarFormulario(request, response, "novo", null, null);
        } else if (request.getParameter("editar") != null) {
            editar(request, response, logado);
        } else if (request.getParameter("detalhes") != null) {
            detalhes(request, response, logado);
        } else {
            mostrarLista(request, response);
        }
    }

    private void editar(HttpServletRequest request, HttpServletResponse response, Usuario logado)
            throws ServletException, IOException {
        Filial item = filialService.buscarParaEdicao(ParametrosUtils.idOuZero(request.getParameter("editar")), logado);
        if (item == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        mostrarFormulario(request, response, "editar", item, filialService.buscarEndereco(item));
    }

    private void detalhes(HttpServletRequest request, HttpServletResponse response, Usuario logado)
            throws ServletException, IOException {
        Filial item = filialService.buscarVisivel(ParametrosUtils.idOuZero(request.getParameter("detalhes")), logado);
        if (item == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("item", item);
        request.setAttribute("endereco", filialService.buscarEndereco(item));
        request.setAttribute("segmento", filialService.buscarSegmento(item));
        request.setAttribute("resumo", filialDetalhesService.montarResumo(item.getId()));
        request.setAttribute("listaInversores", filialDetalhesService.listarInversores(item.getId()));
        request.setAttribute("equipe", filialDetalhesService.listarEquipe(item.getId()));
        request.getRequestDispatcher(VIEW_DETALHES).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);
        String acao = request.getParameter("acao") == null ? "cadastrar" : request.getParameter("acao");
        int id = ParametrosUtils.idOuZero(request.getParameter("id"));

        String erro = switch (acao) {
            case "cadastrar" -> filialService.cadastrarFilial(lerFilial(request, 0), lerEndereco(request), logado);
            case "editar" -> filialService.editarFilial(lerFilial(request, id), lerEndereco(request), logado);
            case "inativar" -> filialService.inativarFilial(id, logado);
            case "reativar" -> filialService.reativarFilial(id, logado);
            default -> "Ação inválida";
        };

        if (erro == null) {
            response.sendRedirect(request.getContextPath() + "/filial");
            return;
        }

        request.setAttribute("erroFilial", erro);
        if (acao.equals("cadastrar") || acao.equals("editar")) {
            boolean editando = acao.equals("editar");
            mostrarFormulario(request, response, editando ? "editar" : "novo", lerFilial(request, editando ? id : 0), lerEndereco(request));
        } else {
            mostrarLista(request, response);
        }
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("listaFiliais", filialService.listarVisiveis(SessaoUtils.usuarioLogado(request)));
        request.getRequestDispatcher(VIEW_LISTA).forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, String modo, Filial item, Endereco endereco)
            throws ServletException, IOException {
        request.setAttribute("modo", modo);
        request.setAttribute("item", item);
        request.setAttribute("endereco", endereco);
        request.setAttribute("listaSegmentos", segmentoService.listarTodos());
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    // segmento inválido vira 0 (id que não existe) e o Service responde com a mensagem de erro
    private Filial lerFilial(HttpServletRequest request, int id) {
        Filial filial = new Filial();
        filial.setId(id);
        filial.setNome(request.getParameter("nome"));
        filial.setIdSegmento(ParametrosUtils.idOuZero(request.getParameter("segmento")));
        filial.setCnpj(request.getParameter("cnpj"));
        filial.setEmail(request.getParameter("email"));
        filial.setTelefone(request.getParameter("telefone"));
        return filial;
    }

    private Endereco lerEndereco(HttpServletRequest request) {
        Endereco endereco = new Endereco();
        endereco.setCep(request.getParameter("cep"));
        endereco.setLogradouro(request.getParameter("logradouro"));
        endereco.setNumero(request.getParameter("numero"));
        endereco.setComplemento(request.getParameter("complemento"));
        endereco.setBairro(request.getParameter("bairro"));
        endereco.setCidade(request.getParameter("cidade"));
        endereco.setEstado(request.getParameter("estado"));
        return endereco;
    }
}
