package org.apollo.apollowebcrud.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apollo.apollowebcrud.model.Empresa;
import org.apollo.apollowebcrud.model.Endereco;
import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;
import org.apollo.apollowebcrud.service.EmpresaService;
import org.apollo.apollowebcrud.util.SessaoUtils;

import java.io.IOException;

// GET /empresa:
//   - gerente sem empresa: já abre o formulário vazio (modo "novo")
//   - ?editar=1: formulário preenchido com a empresa do gerente (modo "editar")
//   - caso contrário: a tela da empresa (listaEmpresas)
// POST com o campo "acao": cadastrar (padrão) | editar
// Formulário: modo, item (Empresa) e endereco (Endereco)
@WebServlet("/empresa")
public class EmpresaServlet extends HttpServlet {

    private static final String VIEW_EMPRESA = "/WEB-INF/views/empresa.jsp";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/cadastro-empresa.jsp";

    private final EmpresaService empresaService = new EmpresaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);

        if (gerenteSemEmpresa(logado)) {
            mostrarFormulario(request, response, "novo", null, null);
        } else if (request.getParameter("editar") != null) {
            editar(request, response, logado);
        } else {
            request.setAttribute("listaEmpresas", empresaService.listarVisiveis(logado));
            request.getRequestDispatcher(VIEW_EMPRESA).forward(request, response);
        }
    }

    private void editar(HttpServletRequest request, HttpServletResponse response, Usuario logado)
            throws ServletException, IOException {
        Empresa item = empresaService.buscarDoGerente(logado);
        if (item == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        mostrarFormulario(request, response, "editar", item, empresaService.buscarEndereco(item));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario logado = SessaoUtils.usuarioLogado(request);
        boolean editando = "editar".equals(request.getParameter("acao"));
        Empresa dados = lerEmpresa(request);
        Endereco endereco = lerEndereco(request);

        String erro = editando
                ? empresaService.editarEmpresa(dados, endereco, logado)
                : empresaService.cadastrarEmpresa(dados, endereco, logado);

        if (erro == null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.setAttribute("erroEmpresa", erro);
        mostrarFormulario(request, response, editando ? "editar" : "novo", dados, endereco);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, String modo, Empresa item, Endereco endereco)
            throws ServletException, IOException {
        request.setAttribute("modo", modo);
        request.setAttribute("item", item);
        request.setAttribute("endereco", endereco);
        request.getRequestDispatcher(VIEW_FORMULARIO).forward(request, response);
    }

    private boolean gerenteSemEmpresa(Usuario logado) {
        return logado.getPerfil() == TipoUsuEnum.GERENTE && logado.getIdEmpresa() == null;
    }

    private Empresa lerEmpresa(HttpServletRequest request) {
        Empresa empresa = new Empresa();
        empresa.setNome(request.getParameter("nome"));
        empresa.setDescricao(request.getParameter("descricao"));
        empresa.setCnpj(request.getParameter("cnpj"));
        empresa.setEmailContato(request.getParameter("emailContato"));
        empresa.setUrlFoto(request.getParameter("urlFoto"));
        return empresa;
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
