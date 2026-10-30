<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Apollo | Código via email</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
<!-- login.css traz o que as telas têm em comum (banner, painel, campos, botão) -->
<link rel="stylesheet" href="${raiz}/docs/css/login/login.css">
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
      <h1 class="titulo">Código via email</h1>
      <p class="instrucao">Digite o código que enviamos para o email: <strong id="email-destino"><c:out value="${emailRecuperacao}" default="seu email"/></strong></p>

      <form id="form-codigo" action="${raiz}/recuperar-senha" method="post" novalidate>
        <input type="hidden" name="acao" value="redefinir">
        <input type="hidden" name="email" value="<c:out value='${emailRecuperacao}'/>">
        <input class="campo campo-codigo" type="text" name="codigo" placeholder="Código" inputmode="numeric" maxlength="6" autocomplete="one-time-code" aria-label="Código de 6 números" required>
        <input class="campo" type="password" name="novaSenha" placeholder="Nova senha" autocomplete="new-password" aria-label="Nova senha" required>
        <input class="campo" type="password" name="confirmacaoSenha" placeholder="Confirmar nova senha" autocomplete="new-password" aria-label="Confirmar nova senha" required>
        <button class="entrar" type="submit">
          <span class="spinner" aria-hidden="true"></span>
          <span class="entrar-txt">Enviar</span>
        </button>
        <p class="erro" id="erro" role="alert"><c:out value="${erroRecuperacao}"/></p>
      </form>

      <a class="voltar" href="${raiz}/recuperar-senha">Não recebeu? Voltar e tentar de novo</a>
    </div>
  </section>

</main>

<script src="${raiz}/docs/js/login-codigo.js"></script>
</body>
</html>