<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Dashboard - Apollo</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;600;700&family=Plus+Jakarta+Sans:wght@600&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="viewport">
    <main class="tela" id="tela">

      <h1 class="titulo">Dashboard</h1>
      <span class="tag-controle"><span>Controle geral</span></span>

      <button class="btn-notificacao" aria-label="Notificações">
        <span class="sino"><img data-asset="notificacao.png" alt=""></span>
        <img class="notif-ponto" data-asset="notificacao-ponto.svg" alt="">
      </button>
      <img class="avatar" data-asset="avatar.png" alt="Foto do usuário">
      <p class="usuario-nome">Neymar júnior césar</p>
      <button class="btn-menu" id="btnMenu" aria-label="Menu do usuário" aria-expanded="false">
        <img data-asset="menu-down.svg" alt="">
      </button>
      <div class="menu-usuario" id="menuUsuario" hidden>
        <a href="#">Config. Perfil</a>
        <a href="#">Sair</a>
      </div>

      <section class="card c-membros">
        <img class="ico ico-membros" data-asset="icon-membros.svg" alt="">
        <p class="card-label l-membros">Total de membros</p>
        <p class="card-valor v-membros">58</p>
        <span class="badge b-membros"><span>+3 novos membros</span></span>
      </section>

      <section class="card c-lotes">
        <span class="ico-lotes-wrap"><img data-asset="icon-lotes.svg" alt=""></span>
        <p class="card-label l-lotes">Total de Lotes</p>
        <p class="card-valor v-lotes">6</p>
      </section>

      <section class="card c-potencia">
        <span class="ico-potencia"><img data-asset="icon-potencia.svg" alt=""></span>
        <p class="card-label l-potencia">Potência total</p>
        <p class="card-valor v-potencia">8,4 kWp</p>
        <span class="badge b-potencia"><span>Aumento de 10%</span></span>
      </section>

      <section class="card c-paineis">
        <img class="ico-paineis" data-asset="icon-paineis.svg" alt="">
        <p class="card-label l-paineis">Paineis totais</p>
        <p class="card-valor v-paineis">128</p>
        <span class="badge b-paineis"><span>+10 novos painéis</span></span>
      </section>

      <section class="card c-grafico">
        <img class="ico-folha" data-asset="icon-folha.svg" alt="">
        <p class="grafico-titulo">Aumento na energia gerada pelos lotes</p>
        <img class="grafico-area" data-asset="grafico.svg" alt="Gráfico de energia gerada por mês">
        <div class="grafico-linha"></div>
        <div class="grafico-meses" id="meses"></div>
      </section>

      <section class="card c-alcancadas">
        <img class="ico-meta ico-alcancadas" data-asset="icon-metas-alcancadas.svg" alt="">
        <p class="meta-titulo t-alcancadas">Metas alcançadas</p>
        <div class="meta-linha linha-verde"></div>
        <button class="meta-item m1">Meta 1</button>
        <button class="meta-item m2">Meta 2</button>
      </section>

      <section class="card c-pendentes">
        <img class="ico-meta ico-pendentes" data-asset="icon-metas-pendentes.svg" alt="">
        <p class="meta-titulo t-pendentes">Metas Pendentes</p>
        <div class="meta-linha linha-amarela"></div>
        <button class="meta-item m3">Meta 3</button>
        <button class="meta-item m4">Meta 4</button>
        <button class="meta-item m5">Meta 5</button>
      </section>

      <aside class="sidebar">
        <img class="logo" data-asset="logo-apollo.svg" alt="Apollo">

        <nav>
          <a href="dashboard.html" class="nav-item n-dashboard ativo">
            <img class="nav-ico i-dashboard" data-asset="nav-dashboard.svg" alt=""><span>Dashboard</span>
          </a>
          <a href="lotes.html" class="nav-item n-lotes">
            <img class="nav-ico i-lotes" data-asset="nav-lotes.svg" alt=""><span>Lotes</span>
          </a>
          <a href="manutencoes.html" class="nav-item n-manut">
            <img class="nav-ico i-manut" data-asset="nav-manutencoes.svg" alt=""><span>Manutenções</span>
          </a>
          <a href="filial.html" class="nav-item n-filial">
            <img class="nav-ico i-filial" data-asset="nav-filial.svg" alt=""><span>Filial</span>
          </a>
          <a href="usuarios.html" class="nav-item n-usuarios">
            <img class="nav-ico i-usuarios" data-asset="nav-usuarios.svg" alt=""><span>Usuários</span>
          </a>
          <a href="contrato.html" class="nav-item n-contrato">
            <img class="nav-ico i-contrato" data-asset="nav-contrato.svg" alt=""><span>Contrato</span>
          </a>
          <a href="empresa.html" class="nav-item n-empresa">
            <img class="nav-ico i-empresa" data-asset="nav-empresa.svg" alt=""><span>Empresa</span>
          </a>
        </nav>

        <a href="configuracoes.html" class="nav-config">
          <img data-asset="nav-configuracoes.svg" alt=""><span>Configurações</span>
        </a>
      </aside>

    </main>
  </div>
  <script src="script.js"></script>
</body>
</html>
