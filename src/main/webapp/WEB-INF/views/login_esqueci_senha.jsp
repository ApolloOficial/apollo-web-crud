<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Apollo | Esqueci a senha</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
<!-- login.css traz o que as duas telas têm em comum (banner, painel, campos, botão) -->
<link rel="stylesheet" href="${raiz}/docs/css/login/login.css">
<link rel="stylesheet" href="${raiz}/docs/css/login/login_esqueci_senha.css">
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
      <h1 class="titulo">Esqueci a senha</h1>
      <p class="instrucao">Digite seu email<br>enviaremos um código.</p>


      <form id="form-esqueci" action="${raiz}/recuperar-senha" method="post" novalidate>
        <input type="hidden" name="acao" value="enviar">
        <input class="campo" type="email" name="email" placeholder="Email" value="<c:out value='${emailRecuperacao}'/>" autocomplete="username" aria-label="Email" required>
        <button class="entrar" type="submit"><span class="entrar-txt">Enviar</span></button>
        <p class="erro" id="erro" role="alert"><c:out value="${erroRecuperacao}"/></p>
      </form>


      <a class="voltar" href="${raiz}/login">Voltar</a>
    </div>
  </section>


</main>


<script src="${raiz}/docs/js/esqueci-senha.js"></script>
</body>
</html>