<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="painel"/>
<c:set var="podeEditar" value="${usuarioLogado.perfil != 'GERENTE'}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | Painéis</title>
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
        <ap:topo titulo="Painéis" subtitulo="Painéis da sua filial e a string de cada um"/>


        <div class="barra">
            <div class="barra-filtros">
                <input class="filtro" type="search" id="busca" placeholder="Buscar por série, marca, modelo ou string" aria-label="Buscar painel">
                <select class="filtro" id="filtro-status" aria-label="Filtrar por status">
                    <option value="">Todos os status</option>
                    <option value="ATIVO">Ativos</option>
                    <option value="INATIVO">Inativos</option>
                </select>
            </div>
            <c:if test="${podeEditar}">
                <a class="btn" href="${raiz}/painel?novo=1">Cadastrar painel</a>
            </c:if>
        </div>

        <div class="tabela-wrap">
            <table class="tabela">
                <thead>
                <tr><th>Nº de série</th><th>String</th><th>Marca</th><th>Modelo</th><th>Potência (W)</th><th>Nota fiscal</th><th>Instalação</th><th>Status</th><th>Ações</th></tr>
                </thead>
                <tbody>
                <c:forEach var="p" items="${listaPaineis}">
                    <tr data-linha data-status="${p.status}">
                        <td><c:out value="${p.numeroSerie}"/></td>
                        <td><c:out value="${codigosString[p.idString]}" default="—"/></td>
                        <td><c:out value="${p.marca}"/></td>
                        <td><c:out value="${p.modelo}"/></td>
                        <td><c:out value="${p.potenciaW}" default="—"/></td>
                        <td><c:out value="${p.notaFiscal}" default="—"/></td>
                        <td><ap:data valor="${p.dataInstalacao}"/></td>
                        <td><ap:badge valor="${p.status}"/></td>
                        <td>
                            <div class="acoes">
                                <c:if test="${podeEditar}">
                                    <a class="btn btn-mini btn-contorno" href="${raiz}/painel?editar=${p.id}">Editar</a>
                                </c:if>
                                <a class="btn btn-mini btn-contorno" href="${raiz}/string?detalhes=${p.idString}">Ver string</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty listaPaineis}">
                <p class="vazio">Nenhum painel cadastrado.</p>
            </c:if>
            <p class="vazio" id="sem-resultado" hidden>Nenhum painel encontrado para o filtro informado.</p>
        </div>
        <c:set var="erroAviso" value=""/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/filtro-tabela.js"></script>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
