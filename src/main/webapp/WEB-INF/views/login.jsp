<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Apollo | Login</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${raiz}/docs/css/login/login.css">
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
      <h1 class="titulo">Bem vindo</h1>
      <p class="sub">Selecione sua área para efetuar o login</p>

      <div class="areas" role="group" aria-label="Área de acesso">
        <button type="button" class="area" data-area="gerente" aria-pressed="${areaSelecionada eq 'gerente'}">Gerente</button>
        <button type="button" class="area" data-area="tecnico" aria-pressed="${areaSelecionada eq 'tecnico'}">Técnico</button>
        <button type="button" class="area" data-area="operador" aria-pressed="${areaSelecionada eq 'operador'}">Operador</button>
      </div>

      <%-- O servidor compara a área escolhida com o perfil associado à conta no banco. --%>
      <form id="form-login" action="${raiz}/login" method="post" novalidate>
        <input type="hidden" name="area" id="area" value="<c:out value='${areaSelecionada}'/>">
        <input class="campo" type="email" name="email" value="<c:out value='${emailDigitado}'/>" placeholder="Email" autocomplete="username" aria-label="Email" required>
        <input class="campo" type="password" name="senha" placeholder="Senha" autocomplete="current-password" aria-label="Senha" required>
        <a class="esqueci" href="${raiz}/recuperar-senha">Esqueci a senha</a>
        <button class="entrar" type="submit">Entrar</button>
        <c:if test="${not empty erroLogin}">
          <p class="erro" id="erro" role="alert">${erroLogin}</p>
        </c:if>
      </form>
    </div>
  </section>

</main>
<script src="${raiz}/docs/js/login-area.js" defer></script>
</body>
</html>
