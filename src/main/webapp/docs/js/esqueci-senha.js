(function () {
  'use strict';

  var EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  var form = document.getElementById('form-esqueci');
  var wrap = document.querySelector('.form-wrap');
  var erro = document.getElementById('erro');
  var campo = form.elements.email;
  var botao = form.querySelector('.entrar');
  var rotulo = botao.querySelector('.entrar-txt');
  var enviando = false;

  function mostrarErro(msg) {
    erro.textContent = msg;
    campo.focus();
    wrap.classList.remove('tremer');
    void wrap.offsetWidth;
    wrap.classList.add('tremer');
  }

  campo.addEventListener('input', function () { erro.textContent = ''; });
  wrap.addEventListener('animationend', function () { wrap.classList.remove('tremer'); });

  form.addEventListener('submit', function (e) {
    if (enviando) {
      e.preventDefault();
      return;
    }
    var email = campo.value.trim();
    if (!email) {
      e.preventDefault();
      mostrarErro('Digite seu email.');
      return;
    }
    if (!EMAIL_RE.test(email)) {
      e.preventDefault();
      mostrarErro('Email inválido. Confira e tente de novo.');
      return;
    }
    campo.value = email;
    enviando = true;
    botao.classList.add('carregando');
    rotulo.textContent = 'Enviando…';
  });

  window.addEventListener('pageshow', function (e) {
    if (e.persisted) {
      enviando = false;
      botao.classList.remove('carregando');
      rotulo.textContent = 'Enviar';
    }
  });
})();
