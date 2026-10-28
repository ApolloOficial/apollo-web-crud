(function () {
  'use strict';

  var TAMANHO = 6;                         // quantidade de números do código

  var $ = function (id) { return document.getElementById(id); };
  var form = $('form-codigo');
  var wrap = document.querySelector('.form-wrap');
  var erro = $('erro');
  var campo = form.elements.codigo;
  var botao = form.querySelector('.entrar');
  var rotulo = botao.querySelector('.entrar-txt');
  var enviando = false;

  /* ---------- Mostra para qual email o código foi (parcialmente escondido) ---------- */
  var email = new URLSearchParams(location.search).get('email');

  function mascarar(e) {
    var p = e.split('@');
    if (p.length !== 2 || !p[0]) return e;
    return p[0].slice(0, Math.min(2, p[0].length)) + '***@' + p[1];
  }

  if (email) {
    $('email-destino').textContent = mascarar(email);
    // manda o email junto para o servidor saber de quem é o código
    var oculto = document.createElement('input');
    oculto.type = 'hidden';
    oculto.name = 'email';
    oculto.value = email;
    form.appendChild(oculto);
  }

  /* ---------- Campo: só números, no máximo 6 (vale também para colar) ---------- */
  campo.maxLength = TAMANHO;
  campo.addEventListener('input', function () {
    campo.value = campo.value.replace(/\D/g, '').slice(0, TAMANHO);
    limparErro();
  });

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

  wrap.addEventListener('animationend', function () { wrap.classList.remove('tremer'); });

  function ocupado(sim) {
    enviando = sim;
    botao.disabled = sim;
    botao.classList.toggle('carregando', sim);
    botao.setAttribute('aria-busy', String(sim));
    rotulo.textContent = sim ? 'Verificando…' : 'Enviar';
  }

  form.addEventListener('submit', function (e) {
    e.preventDefault();
    if (enviando) return;

    var codigo = campo.value;
    if (!codigo)                  { mostrarErro('Digite o código que você recebeu.'); return; }
    if (codigo.length < TAMANHO)  { mostrarErro('O código tem ' + TAMANHO + ' números.'); return; }

    limparErro();
    ocupado(true);
    form.submit();                         // POST em "validar-codigo"
  });

  window.addEventListener('pageshow', function (e) { if (e.persisted) ocupado(false); });

  if (window.matchMedia('(min-width: 821px)').matches) campo.focus();
})();