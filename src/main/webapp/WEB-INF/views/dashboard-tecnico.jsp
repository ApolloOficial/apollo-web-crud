<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Apollo | Dashboard</title>

  <!-- Fonte do layout (Poppins); se não carregar, cai para Arial do apollostyle.css -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap">

  <!-- Arquivos enviados (mantenha na mesma pasta; ajuste os nomes se forem diferentes) -->
  <link rel="stylesheet" href="${raiz}/docs/css/apollostyle.css">
  <link rel="stylesheet" href="${raiz}/docs/css/logoApollo.css">
  <link rel="stylesheet" href="${raiz}/docs/css/pop-ups.css">
  <!-- Complemento desta tela -->
  <link rel="stylesheet" href="${raiz}/docs/css/dashboard.css">
  <script src="${raiz}/docs/js/pop-ups.js" defer></script>
    <link rel="stylesheet" href="${raiz}/docs/css/sidebar.css">
</head>
<body>
<div class="pagina">

    <!-- ===== Sidebar (montada por sidebar.js conforme o tipo de usuário) ===== -->
    <aside class="sidebar" data-ativo="dashboard" data-tipo="tecnico,operador"></aside>
    <script src="${raiz}/docs/js/sidebar.js?v=4"></script>

  <!-- ===== Conteúdo ===== -->
  <main class="conteudo">
    <header class="topo">
      <h1 class="titulo">Dashboard</h1>
      <div class="usuario">
        <button type="button" class="sino" aria-label="Notificações">
          <svg viewBox="0 0 24 24"><path d="M6 8a6 6 0 0 1 12 0c0 7 3 8 3 8H3s3-1 3-8"/><path d="M10 21a2 2 0 0 0 4 0"/></svg>
        </button>
        <span class="avatar" aria-hidden="true"><c:out value="${usuarioLogado.iniciais}"/></span>
        <span class="nome-usuario"><c:out value="${usuarioLogado.nome}"/></span>
      </div>
    </header>

    <span class="status-titulo laranja">Controle geral</span>

    <div class="dash-grade">

      <!-- Cards de resumo -->
      <section class="resumo" aria-label="Resumo">
        <article class="card">
          <div class="card-titulo">
            <svg viewBox="0 0 24 24"><circle cx="9" cy="8" r="3.5"/><path d="M2.5 20c.5-4 3-6 6.5-6s6 2 6.5 6"/><circle cx="17" cy="9" r="2.5"/><path d="M17 14c2.5 0 4 1.7 4.5 5"/></svg>
            Total de membros
          </div>
          <div class="card-numero">${dashboard.totalMembros}</div>
        </article>

        <article class="card">
          <div class="card-titulo">
            <svg viewBox="0 0 24 24"><path d="m5 10 2-6h10l2 6H5Z"/><path d="M4 10h16v6H4zM12 4v12M8 10v6M16 10v6M12 16v4M8 20h8"/></svg>
            Manutenções
          </div>
          <div class="card-numero">${dashboard.totalManutencoes}</div>
        </article>

        <article class="card">
          <div class="card-titulo">
            <svg viewBox="0 0 24 24"><path d="M13 2 4 14h7l-1 8 9-12h-7l1-8Z"/></svg>
            Potência total
          </div>
          <div class="card-numero"><fmt:formatNumber value="${dashboard.potenciaTotalKw}" minFractionDigits="1" maxFractionDigits="1"/> kWp</div>
        </article>

        <article class="card">
          <div class="card-titulo">
            <svg viewBox="0 0 24 24"><path d="M12 3 4 7v10l8 4 8-4V7l-8-4Z"/><path d="m4 7 8 4 8-4M12 11v10"/></svg>
            Total de inversores
          </div>
          <div class="card-numero">${dashboard.totalInversores}</div>
        </article>
      </section>


    </div>
  </main>
</div>


</body>
</html>