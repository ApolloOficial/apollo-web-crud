<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Apollo | Nova senha</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
<!-- login.css traz o que as telas têm em comum (banner, painel, campos, botão) -->
<link rel="stylesheet" href="../../docs/css/login/login.css">
<link rel="stylesheet" href="../../docs/css/login/login_nova_senha.css">
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
      <h1 class="titulo">Nova senha</h1>
      <p class="instrucao">Digite sua nova senha.</p>

      <form id="form-nova" action="nova-senha" method="post" novalidate>
        <div class="campo-wrap">
          <input class="campo" type="password" name="senha" placeholder="Nova senha" autocomplete="new-password" aria-label="Nova senha" required>
          <button type="button" class="ver-senha" aria-label="Mostrar senha" aria-pressed="false">
            <svg viewBox="0 0 24 24" aria-hidden="true">
              <path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7S2 12 2 12Z"/>
              <circle cx="12" cy="12" r="3"/>
              <path class="riscado" d="m4 4 16 16"/>
            </svg>
          </button>
        </div>

        <div class="campo-wrap">
          <input class="campo" type="password" name="confirmar" placeholder="Confirme a nova senha" autocomplete="new-password" aria-label="Confirme a nova senha" required>
          <button type="button" class="ver-senha" aria-label="Mostrar senha" aria-pressed="false">
            <svg viewBox="0 0 24 24" aria-hidden="true">
              <path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7S2 12 2 12Z"/>
              <circle cx="12" cy="12" r="3"/>
              <path class="riscado" d="m4 4 16 16"/>
            </svg>
          </button>
        </div>
        <p class="aviso" id="aviso-caps" hidden>Caps Lock está ativado</p>

        <button class="entrar" type="submit">
          <span class="spinner" aria-hidden="true"></span>
          <span class="entrar-txt">Salvar</span>
        </button>
        <p class="erro" id="erro" role="alert"></p>
      </form>

      <a class="voltar" href="login.html">Voltar</a>
    </div>
  </section>

</main>

<script src="../../docs/js/nova-senha.js"></script>
</body>
</html>