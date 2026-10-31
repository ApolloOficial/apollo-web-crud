<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="manutencao"/>
<c:set var="podeEditar" value="${usuarioLogado.perfil != 'GERENTE'}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | Manutenções</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${raiz}/docs/css/logoApollo.css">
    <link rel="stylesheet" href="${raiz}/docs/css/sidebar.css">
    <link rel="stylesheet" href="${raiz}/docs/css/apollo.css">
</head>
<body>
<div class="pagina">
    <%@ include file="includes/sidebar.jspf" %>

    <main class="conteudo">
        <ap:topo titulo="Manutenções" subtitulo="Ordens de manutenção dos painéis da sua filial"/>


        <div class="barra">
            <div class="barra-filtros">
                <input class="filtro" type="search" id="busca" placeholder="Buscar por painel, descrição ou responsável" aria-label="Buscar manutenção">
                <select class="filtro" id="filtro-status" aria-label="Filtrar por status">
                    <option value="">Todos os status</option>
                    <option value="ABERTA">Abertas</option>
                    <option value="EM_ANDAMENTO">Em andamento</option>
                    <option value="CONCLUIDA">Concluídas</option>
                    <option value="CANCELADA">Canceladas</option>
                </select>
            </div>
            <c:if test="${podeEditar}">
                <a class="btn" href="${raiz}/manutencao?novo=1">Cadastrar manutenção</a>
            </c:if>
        </div>

        <div class="tabela-wrap">
            <table class="tabela">
                <thead>
                <tr><th>Painel</th><th>Tipo</th><th>Descrição</th><th>Status</th><th>Abertura</th><th>Previsão</th><th>Conclusão</th><th>Responsável</th><th>Ações</th></tr>
                </thead>
                <tbody>
                <c:forEach var="m" items="${listaManutencoes}">
                    <tr data-linha data-status="${m.status}">
                        <td><c:out value="${seriesPaineis[m.idPainel]}" default="—"/></td>
                        <td><ap:badge valor="${m.tipoManutencao}"/></td>
                        <td><c:out value="${m.descricao}" default="—"/></td>
                        <td><ap:badge valor="${m.status}"/></td>
                        <td><ap:data valor="${m.dataAbertura}"/></td>
                        <td><ap:data valor="${m.dataPrevista}"/></td>
                        <td><ap:data valor="${m.dataConclusao}"/></td>
                        <td><c:out value="${nomesResponsaveis[m.idUsuarioResponsavel]}" default="—"/></td>
                        <td>
                            <div class="acoes">
                                <c:if test="${podeEditar}">
                                    <a class="btn btn-mini btn-contorno" href="${raiz}/manutencao?editar=${m.id}">Atualizar</a>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty listaManutencoes}">
                <p class="vazio">Nenhuma manutenção cadastrada.</p>
            </c:if>
            <p class="vazio" id="sem-resultado" hidden>Nenhuma manutenção encontrada para o filtro informado.</p>
        </div>
        <c:set var="erroAviso" value=""/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/filtro-tabela.js"></script>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
