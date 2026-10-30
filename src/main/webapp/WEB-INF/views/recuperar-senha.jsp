<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Apollo | Recuperar senha</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${raiz}/docs/css/login/login.css">
<link rel="stylesheet" href="${raiz}/docs/css/login/login_esqueci_senha.css">
<link rel="stylesheet" href="${raiz}/docs/css/login/login_codigo.css">
</head>
<body>
<main class="tela">

  <section class="banner">
    <div class="logo">
      <img class="logo-texto" src="${raiz}/docs/img/logo/Union.png" alt="Apollo">
      <img class="logo-seta" src="${raiz}/docs/img/logo/Group 18.png" alt="" aria-hidden="true">
    </div>
    <h2>Cadastro, monitoramento, gestão e muito mais</h2>
    <p class="legal"><a href="#">Termos de serviço</a> / <a href="#">políticas de privacidade</a></p>
  </section>

  <section class="painel">
    <div class="form-wrap">

      <%--
        Antes esta tela era servida como login_esqueci_senha.jsp + login_codigo.jsp,
        duas páginas navegando por link estático. O RecuperacaoSenhaServlet sempre
        faz forward para /WEB-INF/views/recuperar-senha.jsp e decide a etapa pelo
        atributo "etapa" ("email" ou "codigo"), então as duas foram unificadas aqui.
      --%>

      <c:choose>

        <%-- Etapa 1: pedir o e-mail --%>
        <c:when test="${etapa == 'email'}">
          <h1 class="titulo">Esqueci a senha</h1>
          <p class="instrucao">Digite seu email<br>enviaremos um código.</p>

          <form id="form-esqueci" action="${raiz}/recuperar-senha" method="post" novalidate>
            <input type="hidden" name="acao" value="enviar">
            <input class="campo" type="email" name="email" placeholder="Email"
                   value="${emailRecuperacao}" autocomplete="username" aria-label="Email" required>
            <button class="entrar" type="submit">Enviar</button>
            <c:if test="${not empty erroRecuperacao}">
              <p class="erro" role="alert">${erroRecuperacao}</p>
            </c:if>
          </form>

          <a class="voltar" href="${raiz}/login">Voltar</a>
        </c:when>

        <%-- Etapa 2: código recebido por e-mail + nova senha --%>
        <c:otherwise>
          <h1 class="titulo">Código via email</h1>
          <p class="instrucao">Digite o código que enviamos para o email:
            <strong><c:out value="${emailRecuperacao}"/></strong>
          </p>

          <form id="form-codigo" action="${raiz}/recuperar-senha" method="post" novalidate>
            <input type="hidden" name="acao" value="redefinir">
            <input type="hidden" name="email" value="${emailRecuperacao}">

            <input class="campo campo-codigo" type="text" name="codigo" placeholder="Código"
                   inputmode="numeric" maxlength="6" autocomplete="one-time-code"
                   aria-label="Código de 6 números" required>
            <input class="campo" type="password" name="novaSenha" placeholder="Nova senha"
                   autocomplete="new-password" aria-label="Nova senha" required>
            <input class="campo" type="password" name="confirmacaoSenha" placeholder="Confirmar nova senha"
                   autocomplete="new-password" aria-label="Confirmar nova senha" required>

            <button class="entrar" type="submit">
              <span class="spinner" aria-hidden="true"></span>
              <span class="entrar-txt">Redefinir senha</span>
            </button>
            <c:if test="${not empty erroRecuperacao}">
              <p class="erro" role="alert">${erroRecuperacao}</p>
            </c:if>
          </form>

          <a class="voltar" href="${raiz}/recuperar-senha">Não recebeu? Voltar e tentar de novo</a>
        </c:otherwise>

      </c:choose>

    </div>
  </section>

</main>
</body>
</html>
