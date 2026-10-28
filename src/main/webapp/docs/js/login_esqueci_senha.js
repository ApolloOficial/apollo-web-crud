(function () {
  'use strict';

  var EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  var $ = function (id) { return document.getElementById(id); };
  var form = $('form-esqueci');
  var wrap = document.querySelector('.form-wrap');
  var erro = $('erro');
  var campo = form.elements.email;
  var botao = form.querySelector('.entrar');
  var rotulo = botao.querySelector('.entrar-txt');
  var enviando = false;

  // se voltou da tela do código, já preenche o email que tinha digitado
  var anterior = new URLSearchParams(location.search).get('email');
  if (anterior) campo.value = anterior;

  function limparErro() {
    erro.textContent = '';
    campo.removeAttribute('aria-invalid');
    campo.removeAttribute('aria-describedby');
  }

  function mostrarErro(msg) {
    limparErro();
    erro.textContent = msg;
    campo.setAttribute('aria-invalid', 'true');
    campo.setAttribute('aria-describedby', 'erro');
    campo.focus();
    wrap.classList.remove('tremer');
    void wrap.offsetWidth;
    wrap.classList.add('tremer');
  }

  campo.addEventListener('input', limparErro);
  wrap.addEventListener('animationend', function () { wrap.classList.remove('tremer'); });

  function ocupado(sim) {
    enviando = sim;
    botao.disabled = sim;
    botao.classList.toggle('carregando', sim);
    botao.setAttribute('aria-busy', String(sim));
    rotulo.textContent = sim ? 'Enviando…' : 'Enviar';
  }

  form.addEventListener('submit', function (e) {
    e.preventDefault();
    if (enviando) return;

    var email = campo.value.trim();
    if (!email)                { mostrarErro('Digite seu email.'); return; }
    if (!EMAIL_RE.test(email)) { mostrarErro('Email inválido. Confira e tente de novo.'); return; }

    limparErro();
    campo.value = email;
    ocupado(true);
    form.submit();                         // GET para login_codigo.html?email=...
  });

  window.addEventListener('pageshow', function (e) { if (e.persisted) ocupado(false); });

  if (window.matchMedia('(min-width: 821px)').matches) campo.focus();
})();