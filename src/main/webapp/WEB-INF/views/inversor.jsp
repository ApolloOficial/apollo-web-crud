<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="inversor"/>
<c:set var="podeEditar" value="${usuarioLogado.perfil != 'GERENTE'}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | Inversores</title>
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
        <ap:topo titulo="Inversores" subtitulo="Inversores da sua filial e suas informações principais"/>


        <div class="barra">
            <div class="barra-filtros">
                <input class="filtro" type="search" id="busca" placeholder="Buscar por código, série, marca ou modelo" aria-label="Buscar inversor">
                <select class="filtro" id="filtro-status" aria-label="Filtrar por status">
                    <option value="">Todos os status</option>
                    <option value="ATIVO">Ativos</option>
                    <option value="INATIVO">Inativos</option>
                </select>
            </div>
            <c:if test="${podeEditar}">
                <a class="btn" href="${raiz}/inversor?novo=1">Cadastrar inversor</a>
            </c:if>
        </div>

        <div class="tabela-wrap">
            <table class="tabela">
                <thead>
                <tr><th>Código</th><th>Nº de série</th><th>Marca</th><th>Modelo</th><th>Potência (kW)</th><th>Instalação</th><th>Status</th><th>Ações</th></tr>
                </thead>
                <tbody>
                <c:forEach var="i" items="${listaInversores}">
                    <tr data-linha data-status="${i.status}">
                        <td><c:out value="${i.codigo}"/></td>
                        <td><c:out value="${i.numeroSerie}"/></td>
                        <td><c:out value="${i.marca}"/></td>
                        <td><c:out value="${i.modelo}"/></td>
                        <td><c:out value="${i.potenciaKw}" default="—"/></td>
                        <td><ap:data valor="${i.dataInstalacao}"/></td>
                        <td><ap:badge valor="${i.status}"/></td>
                        <td>
                            <div class="acoes">
                                <a class="btn btn-mini btn-contorno" href="${raiz}/inversor?detalhes=${i.id}">Detalhes</a>
                                <c:if test="${podeEditar}">
                                    <a class="btn btn-mini btn-contorno" href="${raiz}/inversor?editar=${i.id}">Editar</a>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty listaInversores}">
                <p class="vazio">Nenhum inversor cadastrado.</p>
            </c:if>
            <p class="vazio" id="sem-resultado" hidden>Nenhum inversor encontrado para o filtro informado.</p>
        </div>
        <c:set var="erroAviso" value=""/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/filtro-tabela.js"></script>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
