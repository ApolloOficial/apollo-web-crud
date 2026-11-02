<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="membros"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | Membros</title>
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
        <ap:topo titulo="Membros" subtitulo="Gerencie os usuários da empresa e acompanhe as equipes"/>


        <section class="grade-resumo" aria-label="Resumo">
            <div class="resumo-item"><span>Total de membros</span><strong>${fn:length(listaMembros)}</strong></div>
            <div class="resumo-item"><span>Técnicos</span><strong>${totalTecnicos}</strong></div>
            <div class="resumo-item"><span>Operadores</span><strong>${totalOperadores}</strong></div>
            <div class="resumo-item"><span>Filiais com equipe</span><strong>${totalFiliaisComEquipe}</strong></div>
        </section>

        <div class="barra">
            <div class="barra-filtros">
                <input class="filtro" type="search" id="busca" placeholder="Buscar por nome ou e-mail" aria-label="Buscar membro">
            </div>
            <c:if test="${usuarioLogado.perfil == 'GERENTE' || usuarioLogado.perfil == 'TECNICO'}">
                <a class="btn" href="${raiz}/cadastro">Cadastrar membro</a>
            </c:if>
        </div>

        <div class="tabela-wrap">
            <table class="tabela">
                <thead>
                <tr><th>Membro</th><th>Filial</th><th>Cargo</th><th>E-mail</th><th>Status</th><th>Ações</th></tr>
                </thead>
                <tbody>
                <c:forEach var="m" items="${listaMembros}">
                    <tr data-linha data-status="${m.perfil}">
                        <td>
                            <div class="usuario">
                                <span class="avatar-usuario" aria-hidden="true"><c:out value="${m.iniciais}"/></span>
                                <span><c:out value="${m.nome}"/></span>
                            </div>
                        </td>
                        <td><c:out value="${nomesFiliais[m.idFilial]}" default="—"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${m.perfil == 'GERENTE'}">Gerente</c:when>
                                <c:when test="${m.perfil == 'TECNICO'}">Técnico</c:when>
                                <c:when test="${m.perfil == 'OPERADOR'}">Operador</c:when>
                                <c:otherwise><c:out value="${m.perfil}"/></c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${m.email}"/></td>
                        <td><ap:badge valor="${m.status}"/></td>
                        <td>
                            <div class="acoes">
                                <a class="btn btn-mini btn-contorno" href="${raiz}/membros?editar=${m.id}">Editar</a>
                                <form action="${raiz}/membros" method="post" data-confirmar="Confirma esta alteração de status?">
                                    <input type="hidden" name="id" value="${m.id}">
                                    <c:choose>
                                        <c:when test="${m.status == 'ATIVO'}">
                                            <input type="hidden" name="acao" value="inativar">
                                            <button class="btn btn-mini btn-perigo" type="submit">Inativar</button>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" name="acao" value="reativar">
                                            <button class="btn btn-mini" type="submit">Reativar</button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty listaMembros}">
                <p class="vazio">Nenhum membro cadastrado.</p>
            </c:if>
            <p class="vazio" id="sem-resultado" hidden>Nenhum membro encontrado para o filtro informado.</p>
        </div>
        <c:set var="erroAviso" value="${erroMembros}"/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/filtro-tabela.js"></script>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
