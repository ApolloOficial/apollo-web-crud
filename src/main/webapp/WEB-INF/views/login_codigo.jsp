<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
<link rel="stylesheet" href="../../docs/css/login/login.css">
<link rel="stylesheet" href="../../docs/css/login/login_esqueci_senha.css">
</head>
<body>
<main class="tela">


  <section class="banner">
    <div class="logo">
      <img class="logo-texto" src="../../docs/img/logo/Union.png" alt="Apollo">
      <img class="logo-seta" src="../../docs/img/logo/Group 18.png" alt="" aria-hidden="true">
    </div>
    <h2>Cadastro, monitoramento, gestão e muito mais</h2>
    <p class="legal"><a href="#">Termos de serviço</a> / <a href="#">políticas de privacidade</a></p>
  </section>


  <section class="painel">
    <div class="form-wrap">
      <h1 class="titulo">Código via email</h1>
      <p class="instrucao">Digite o código que enviamos para o email: <span id="email-destino">seu e-mail</span></p>


      <form id="form-codigo" action="validar-codigo" method="post" novalidate>
        <input class="campo" type="text" name="codigo" placeholder="Código" inputmode="numeric" autocomplete="one-time-code" aria-label="Código" required>
        <button class="entrar" type="submit">Enviar</button>
        <p class="erro" id="erro" role="alert"></p>
      </form>
    </div>
  </section>


</main>


<script src="../js/codigo-email.js"></script>
</body>
</html>