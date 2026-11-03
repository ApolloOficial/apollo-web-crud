<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="codigo" value="${pageContext.errorData.statusCode}"/>
<c:if test="${codigo == 0}"><c:set var="codigo" value="500"/></c:if>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | Erro ${codigo}</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${raiz}/docs/css/apollo.css">
</head>
<body>
<main class="erro-tela">
    <span class="codigo">${codigo}</span>
    <c:choose>
        <c:when test="${codigo == 400}">
            <h1>Requisição inválida</h1>
            <p>Os dados enviados não puderam ser entendidos. Volte e confira as informações.</p>
        </c:when>
        <c:when test="${codigo == 403}">
            <h1>Acesso negado</h1>
            <p>Seu perfil não tem permissão para abrir esta página ou executar esta ação.</p>
        </c:when>
        <c:when test="${codigo == 404}">
            <h1>Página não encontrada</h1>
            <p>O endereço não existe ou o registro procurado não está disponível para você.</p>
        </c:when>
        <c:when test="${codigo == 405}">
            <h1>Ação não permitida</h1>
            <p>Esta página não aceita o tipo de requisição enviado.</p>
        </c:when>
        <c:when test="${codigo == 503}">
            <h1>Serviço indisponível</h1>
            <p>Não foi possível validar sua sessão agora. Aguarde alguns instantes e tente novamente.</p>
        </c:when>
        <c:otherwise>
            <h1>Erro interno</h1>
            <p>Algo deu errado do nosso lado. Tente novamente; se continuar, avise a equipe responsável.</p>
        </c:otherwise>
    </c:choose>
    <div class="acoes">
        <c:choose>
            <c:when test="${not empty sessionScope.usuarioLogado}">
                <a class="btn" href="${raiz}/dashboard">Ir para o dashboard</a>
            </c:when>
            <c:otherwise>
                <a class="btn" href="${raiz}/login">Ir para o login</a>
            </c:otherwise>
        </c:choose>
        <a class="btn btn-contorno" href="javascript:history.back()">Voltar</a>
    </div>
</main>
</body>
</html>
