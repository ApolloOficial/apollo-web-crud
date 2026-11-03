<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="filial"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | Filiais</title>
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
        <ap:topo titulo="Filiais" subtitulo="Acompanhe as filiais e abra os detalhes de cada uma"/>


        <div class="barra">
            <div class="barra-filtros">
                <input class="filtro" type="search" id="busca" placeholder="Buscar por nome, CNPJ ou e-mail" aria-label="Buscar filial">
                <select class="filtro" id="filtro-status" aria-label="Filtrar por status">
                    <option value="">Todos os status</option>
                    <option value="ATIVO">Ativas</option>
                    <option value="INATIVO">Inativas</option>
                </select>
            </div>
            <c:if test="${usuarioLogado.perfil == 'GERENTE'}">
                <a class="btn" href="${raiz}/filial?novo=1">Cadastrar filial</a>
            </c:if>
        </div>

        <div class="tabela-wrap">
            <table class="tabela">
                <thead>
                <tr><th>Nome</th><th>CNPJ</th><th>E-mail</th><th>Telefone</th><th>Status</th><th>Ações</th></tr>
                </thead>
                <tbody>
                <c:forEach var="f" items="${listaFiliais}">
                    <tr data-linha data-status="${f.status}">
                        <td><c:out value="${f.nome}"/></td>
                        <td><ap:cnpj valor="${f.cnpj}"/></td>
                        <td><c:out value="${f.email}"/></td>
                        <td><ap:telefone valor="${f.telefone}"/></td>
                        <td><ap:badge valor="${f.status}"/></td>
                        <td>
                            <div class="acoes">
                                <a class="btn btn-mini btn-contorno" href="${raiz}/filial?detalhes=${f.id}">Detalhes</a>
                                <c:if test="${usuarioLogado.perfil == 'GERENTE'}">
                                    <a class="btn btn-mini btn-contorno" href="${raiz}/filial?editar=${f.id}">Editar</a>
                                    <form action="${raiz}/filial" method="post" data-confirmar="Confirma esta alteração de status?">
                                        <input type="hidden" name="id" value="${f.id}">
                                        <c:choose>
                                            <c:when test="${f.status == 'ATIVO'}">
                                                <input type="hidden" name="acao" value="inativar">
                                                <button class="btn btn-mini btn-perigo" type="submit">Inativar</button>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="hidden" name="acao" value="reativar">
                                                <button class="btn btn-mini" type="submit">Reativar</button>
                                            </c:otherwise>
                                        </c:choose>
                                    </form>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty listaFiliais}">
                <p class="vazio">Nenhuma filial cadastrada.</p>
            </c:if>
            <p class="vazio" id="sem-resultado" hidden>Nenhuma filial encontrada para o filtro informado.</p>
        </div>
        <c:set var="erroAviso" value="${erroFilial}"/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/filtro-tabela.js"></script>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
