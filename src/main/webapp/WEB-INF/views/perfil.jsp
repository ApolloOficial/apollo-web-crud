<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="perfil"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | Meu perfil</title>
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
        <ap:topo titulo="Meu perfil" subtitulo="Suas informações cadastrais no Apollo"/>

        <section class="cartao">
            <div class="cabecalho-card">
                <div class="identidade">
                    <div class="logo-card"><c:out value="${usuarioLogado.iniciais}"/></div>
                    <div>
                        <h2><c:out value="${usuarioLogado.nome}"/></h2>
                        <span class="badge badge-aberta">
                            <c:choose>
                                <c:when test="${usuarioLogado.perfil == 'GERENTE'}">Gerente</c:when>
                                <c:when test="${usuarioLogado.perfil == 'TECNICO'}">Técnico</c:when>
                                <c:when test="${usuarioLogado.perfil == 'OPERADOR'}">Operador</c:when>
                                <c:otherwise><c:out value="${usuarioLogado.perfil}"/></c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>
                <c:if test="${usuarioLogado.perfil == 'GERENTE' || usuarioLogado.perfil == 'TECNICO'}">
                    <a class="btn btn-contorno" href="${raiz}/membros?editar=${usuarioLogado.id}">Editar meus dados</a>
                </c:if>
            </div>
        </section>

        <section class="cartao">
            <h2>Informações pessoais</h2>
            <div class="dados">
                <div><span>Nome completo</span><strong><c:out value="${usuarioLogado.nome}"/></strong></div>
                <div><span>CPF</span><strong><ap:cpf valor="${usuarioLogado.cpf}"/></strong></div>
                <div><span>E-mail</span><strong><c:out value="${usuarioLogado.email}"/></strong></div>
                <div><span>Telefone</span><strong><ap:telefone valor="${usuarioLogado.telefone}"/></strong></div>
                <div><span>Status</span><strong><ap:badge valor="${usuarioLogado.status}"/></strong></div>
            </div>
        </section>

        <section class="cartao">
            <h2>Vínculos</h2>
            <div class="dados">
                <div><span>Empresa</span><strong><c:out value="${empresa.nome}" default="Sem empresa vinculada"/></strong></div>
                <div><span>Filial</span><strong><c:out value="${filial.nome}" default="Sem filial vinculada"/></strong></div>
            </div>
        </section>
    </main>
</div>
</body>
</html>
