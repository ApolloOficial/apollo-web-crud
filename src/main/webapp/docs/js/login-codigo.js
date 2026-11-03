(function () {
  'use strict';

  var form = document.getElementById('form-codigo');
  var wrap = document.querySelector('.form-wrap');
  var erro = document.getElementById('erro');
  var campo = form.elements.codigo;
  var senha = form.elements.novaSenha;
  var confirmacao = form.elements.confirmacaoSenha;
  var botao = form.querySelector('.entrar');
  var rotulo = botao.querySelector('.entrar-txt');
  var enviando = false;

  function mostrarErro(msg, alvo) {
    erro.textContent = msg;
    alvo.focus();
    wrap.classList.remove('tremer');
    void wrap.offsetWidth;
    wrap.classList.add('tremer');
  }

  function limparErro() {
    erro.textContent = '';
  }

  wrap.addEventListener('animationend', function () { wrap.classList.remove('tremer'); });

  campo.addEventListener('input', function () {
    campo.value = campo.value.replace(/\D/g, '').slice(0, 6);
    limparErro();
  });
  senha.addEventListener('input', limparErro);
  confirmacao.addEventListener('input', limparErro);

  form.addEventListener('submit', function (e) {
    if (enviando) {
      e.preventDefault();
      return;
    }
    if (!/^\d{6}$/.test(campo.value)) {
      e.preventDefault();
      mostrarErro(campo.value ? 'O código tem 6 números.' : 'Digite o código que você recebeu.', campo);
      return;
    }
    if (senha.value.length < 8) {
      e.preventDefault();
      mostrarErro('A nova senha deve ter pelo menos 8 caracteres.', senha);
      return;
    }
    if (senha.value !== confirmacao.value) {
      e.preventDefault();
      mostrarErro('As senhas não conferem.', confirmacao);
      return;
    }
    enviando = true;
    botao.classList.add('carregando');
    rotulo.textContent = 'Verificando…';
  });

  window.addEventListener('pageshow', function (e) {
    if (e.persisted) {
      enviando = false;
      botao.classList.remove('carregando');
      rotulo.textContent = 'Enviar';
    }
  });
})();
