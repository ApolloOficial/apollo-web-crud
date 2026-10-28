(function () {
  'use strict';

  /* ---------------------------------------------------------------
     MODO DE FUNCIONAMENTO
     false -> não existe servidor de login ainda: valida o formulário
              no navegador e redireciona para o dashboard da área.
     true  -> envia o formulário (POST em "login") para o servidor Java,
              que decide para onde redirecionar.
     --------------------------------------------------------------- */
  var USAR_SERVIDOR = false;

  // Para onde cada área vai depois do login (mesma pasta desta tela)
  var DESTINOS = {
    gerente:  'dashboard-gerente.html',
    tecnico:  'dashboard-tecnico.html',
    operador: 'dashboard-tecnico.html'
  };

  var CHAVE_PERFIL = 'apollo-perfil';        // lida pelo sidebar.js
  var CHAVE_AREA   = 'apollo-ultima-area';   // só para lembrar a última escolha
  var EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  var $ = function (id) { return document.getElementById(id); };
  var form = $('form-login');
  var wrap = document.querySelector('.form-wrap');
  var erro = $('erro');
  var campoEmail = form.elements.email;
  var campoSenha = form.elements.senha;
  var botao = form.querySelector('.entrar');
  var rotulo = botao.querySelector('.entrar-txt');
  var areas = Array.prototype.slice.call(document.querySelectorAll('.area'));
  var aviso = $('aviso-caps');
  var verSenha = $('ver-senha');
  var enviando = false;

  function guardar(k, v) { try { localStorage.setItem(k, v); } catch (e) {} }
  function ler(k) { try { return localStorage.getItem(k); } catch (e) { return null; } }

  /* ---------- Área de acesso (Gerente / Técnico / Operador) ---------- */
  function selecionarArea(nome, focar) {
    if (!DESTINOS[nome]) nome = 'gerente';
    areas.forEach(function (b) {
      var ativo = b.getAttribute('data-area') === nome;
      b.setAttribute('aria-checked', ativo ? 'true' : 'false');
      b.tabIndex = ativo ? 0 : -1;
      if (ativo && focar) b.focus();
    });
    $('area').value = nome;
  }

  areas.forEach(function (b, i) {
    b.addEventListener('click', function () { selecionarArea(b.getAttribute('data-area')); });
    // setas do teclado trocam de área, como num grupo de opções
    b.addEventListener('keydown', function (e) {
      var passo = ({ ArrowRight: 1, ArrowDown: 1, ArrowLeft: -1, ArrowUp: -1 })[e.key];
      if (!passo) return;
      e.preventDefault();
      var prox = areas[(i + passo + areas.length) % areas.length];
      selecionarArea(prox.getAttribute('data-area'), true);
    });
  });

  // ?area=tecnico na URL > última área usada > gerente
  var daUrl = new URLSearchParams(location.search).get('area');
  selecionarArea(DESTINOS[daUrl] ? daUrl : (ler(CHAVE_AREA) || 'gerente'));

  /* ---------- Mensagens de erro ---------- */
  function limparErro() {
    erro.textContent = '';
    [campoEmail, campoSenha].forEach(function (c) {
      c.removeAttribute('aria-invalid');
      c.removeAttribute('aria-describedby');
    });
  }

  function tremer() {
    wrap.classList.remove('tremer');
    void wrap.offsetWidth;                 // reinicia a animação
    wrap.classList.add('tremer');
  }

  function mostrarErro(msg, campo) {
    limparErro();
    erro.textContent = msg;
    if (campo) {
      campo.setAttribute('aria-invalid', 'true');
      campo.setAttribute('aria-describedby', 'erro');
      campo.focus();
    }
    tremer();
  }

  campoEmail.addEventListener('input', limparErro);
  campoSenha.addEventListener('input', limparErro);
  wrap.addEventListener('animationend', function () { wrap.classList.remove('tremer'); });

  /* ---------- Senha: mostrar/ocultar e aviso de Caps Lock ---------- */
  verSenha.addEventListener('click', function () {
    var mostrar = campoSenha.type === 'password';
    campoSenha.type = mostrar ? 'text' : 'password';
    verSenha.setAttribute('aria-pressed', String(mostrar));
    verSenha.setAttribute('aria-label', mostrar ? 'Ocultar senha' : 'Mostrar senha');
    campoSenha.focus();
  });

  function checarCaps(e) {
    if (e.getModifierState) aviso.hidden = !e.getModifierState('CapsLock');
  }
  campoSenha.addEventListener('keydown', checarCaps);
  campoSenha.addEventListener('keyup', checarCaps);
  campoSenha.addEventListener('blur', function () { aviso.hidden = true; });

  /* ---------- Envio ---------- */
  function ocupado(sim) {
    enviando = sim;
    botao.disabled = sim;
    botao.classList.toggle('carregando', sim);
    botao.setAttribute('aria-busy', String(sim));
    rotulo.textContent = sim ? 'Entrando…' : 'Entrar';
  }

  form.addEventListener('submit', function (e) {
    e.preventDefault();
    if (enviando) return;

    var email = campoEmail.value.trim();
    var senha = campoSenha.value;

    if (!email)                  { mostrarErro('Digite seu email.', campoEmail); return; }
    if (!EMAIL_RE.test(email))   { mostrarErro('Email inválido. Confira e tente de novo.', campoEmail); return; }
    if (!senha)                  { mostrarErro('Digite sua senha.', campoSenha); return; }

    limparErro();
    var area = $('area').value;
    guardar(CHAVE_AREA, area);
    ocupado(true);

    if (USAR_SERVIDOR) {
      campoEmail.value = email;
      form.submit();
      return;
    }

    guardar(CHAVE_PERFIL, area);           // o sidebar.js usa isso para montar o menu certo
    setTimeout(function () { location.href = DESTINOS[area]; }, 450);
  });

  // voltar pelo botão "voltar" do navegador não pode deixar o botão travado
  window.addEventListener('pageshow', function (e) { if (e.persisted) ocupado(false); });

  // foco inicial no email (só em tela grande, para não abrir o teclado no celular)
  if (window.matchMedia('(min-width: 821px)').matches) campoEmail.focus();
})();