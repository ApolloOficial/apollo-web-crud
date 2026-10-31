<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="string"/>
<c:set var="podeEditar" value="${usuarioLogado.perfil != 'GERENTE'}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | Strings</title>
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
        <ap:topo titulo="Strings" subtitulo="Strings da sua filial e o inversor de cada uma"/>


        <div class="barra">
            <div class="barra-filtros">
                <input class="filtro" type="search" id="busca" placeholder="Buscar por código ou inversor" aria-label="Buscar string">
                <select class="filtro" id="filtro-status" aria-label="Filtrar por status">
                    <option value="">Todos os status</option>
                    <option value="ATIVO">Ativas</option>
                    <option value="INATIVO">Inativas</option>
                </select>
            </div>
            <c:if test="${podeEditar}">
                <a class="btn" href="${raiz}/string?novo=1">Cadastrar string</a>
            </c:if>
        </div>

        <div class="tabela-wrap">
            <table class="tabela">
                <thead>
                <tr><th>Código</th><th>Inversor</th><th>Nº de MPPT</th><th>Nº de entrada</th><th>Status</th><th>Ações</th></tr>
                </thead>
                <tbody>
                <c:forEach var="s" items="${listaStrings}">
                    <tr data-linha data-status="${s.status}">
                        <td><c:out value="${s.codigo}"/></td>
                        <td><c:out value="${codigosInversor[s.idInversor]}" default="—"/></td>
                        <td><c:out value="${s.numeroMppt}" default="—"/></td>
                        <td><c:out value="${s.numeroEntrada}" default="—"/></td>
                        <td><ap:badge valor="${s.status}"/></td>
                        <td>
                            <div class="acoes">
                                <a class="btn btn-mini btn-contorno" href="${raiz}/string?detalhes=${s.id}">Detalhes</a>
                                <c:if test="${podeEditar}">
                                    <a class="btn btn-mini btn-contorno" href="${raiz}/string?editar=${s.id}">Editar</a>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty listaStrings}">
                <p class="vazio">Nenhuma string cadastrada.</p>
            </c:if>
            <p class="vazio" id="sem-resultado" hidden>Nenhuma string encontrada para o filtro informado.</p>
        </div>
        <c:set var="erroAviso" value=""/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/filtro-tabela.js"></script>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
