<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="pt_BR"/>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="contrato"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | Contrato</title>
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
        <ap:topo titulo="Contrato" subtitulo="O contrato define o período de vigência, o valor mensal e o status da sua prestação de serviço."/>

        <c:choose>
            <c:when test="${empty contrato}">
                <section class="cartao">
                    <p class="vazio">Nenhum contrato encontrado para a sua empresa.</p>
                </section>
            </c:when>
            <c:otherwise>
                <section class="cartao">
                    <h2>Dados do contrato</h2>
                    <div class="dados">
                        <div><span>Empresa</span><strong><c:out value="${empresa.nome}" default="—"/></strong></div>
                        <div><span>Contratante</span><strong><c:out value="${usuarioLogado.nome}"/></strong></div>
                        <div><span>Valor mensal</span><strong><fmt:formatNumber value="${contrato.valorMensal}" type="currency"/></strong></div>
                        <div><span>Data de início</span><strong><ap:data valor="${contrato.dataInicio}"/></strong></div>
                        <div><span>Status</span><strong><ap:badge valor="${contrato.status}"/></strong></div>
                    </div>
                </section>
            </c:otherwise>
        </c:choose>

        <blockquote class="cartao" style="font-style:italic;color:#7a7a7a">
            "Menos estimativa, mais resultado. Renove seu contrato e siga gerindo seus ativos solares com dados reais."
        </blockquote>
    </main>
</div>
</body>
</html>
