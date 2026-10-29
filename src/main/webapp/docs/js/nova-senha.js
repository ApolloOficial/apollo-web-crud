(function () {
  'use strict';

  /* ---------------------------------------------------------------
     MODO DE FUNCIONAMENTO (mesma ideia do login.js e do login-codigo.js)
     false -> sem servidor: só valida no navegador e volta para o login.
              Exige que o código do email tenha sido aceito antes.
     true  -> envia o formulário (POST em "nova-senha") para o servidor
              Java, que confere se o código foi validado e grava a senha.
     --------------------------------------------------------------- */
  var USAR_SERVIDOR = false;

  var MIN_SENHA = 8;                         // tamanho mínimo da senha
  var DESTINO_OK = 'login.html';             // para onde ir depois de salvar
  var DESTINO_SEM_CODIGO = 'login_esqueci_senha.html';

  var $ = function (id) { return document.getElementById(id); };
  var form = $('form-nova');
  var wrap = document.querySelector('.form-wrap');
  var erro = $('erro');
  var campoSenha = form.elements.senha;
  var campoConf = form.elements.confirmar;
  var botao = form.querySelector('.entrar');
  var rotulo = botao.querySelector('.entrar-txt');
  var aviso = $('aviso-caps');
  var enviando = false;

  /* ---------- Quem está trocando a senha ---------- */
  var email = new URLSearchParams(location.search).get('email');
  var CHAVE_OK = 'apollo-codigo-ok:' + String(email || '').toLowerCase();

  function codigoFoiValidado() {
    try { return sessionStorage.getItem(CHAVE_OK) === '1'; } catch (e) { return false; }
  }

  // sem passar pelo código, não tem por que ver esta tela
  if (!USAR_SERVIDOR && (!email || !codigoFoiValidado())) {
    location.replace(DESTINO_SEM_CODIGO);
    return;
  }

  if (email) {
    var oculto = document.createElement('input');
    oculto.type = 'hidden';
    oculto.name = 'email';
    oculto.value = email;
    form.appendChild(oculto);
  }

  /* ---------- Mensagens de erro ---------- */
  function limparErro() {
    erro.textContent = '';
    erro.classList.remove('ok');
    [campoSenha, campoConf].forEach(function (c) {
      c.removeAttribute('aria-invalid');
      c.removeAttribute('aria-describedby');
    });
  }

  function mostrarErro(msg, campo) {
    limparErro();
    erro.textContent = msg;
    if (campo) {
      campo.setAttribute('aria-invalid', 'true');
      campo.setAttribute('aria-describedby', 'erro');
      campo.focus();
    }
    wrap.classList.remove('tremer');
    void wrap.offsetWidth;
    wrap.classList.add('tremer');
  }

  campoSenha.addEventListener('input', limparErro);
  campoConf.addEventListener('input', limparErro);
  wrap.addEventListener('animationend', function () { wrap.classList.remove('tremer'); });

  /* ---------- Mostrar/ocultar senha (um botão por campo) ---------- */
  Array.prototype.forEach.call(document.querySelectorAll('.ver-senha'), function (btn) {
    var alvo = btn.parentNode.querySelector('.campo');
    btn.addEventListener('click', function () {
      var mostrar = alvo.type === 'password';
      alvo.type = mostrar ? 'text' : 'password';
      btn.setAttribute('aria-pressed', String(mostrar));
      btn.setAttribute('aria-label', mostrar ? 'Ocultar senha' : 'Mostrar senha');
      alvo.focus();
    });
  });

  /* ---------- Aviso de Caps Lock ---------- */
  function checarCaps(e) {
    if (e.getModifierState) aviso.hidden = !e.getModifierState('CapsLock');
  }
  [campoSenha, campoConf].forEach(function (c) {
    c.addEventListener('keydown', checarCaps);
    c.addEventListener('keyup', checarCaps);
    c.addEventListener('blur', function () { aviso.hidden = true; });
  });

  /* ---------- Envio ---------- */
  function ocupado(sim) {
    enviando = sim;
    botao.disabled = sim;
    botao.classList.toggle('carregando', sim);
    botao.setAttribute('aria-busy', String(sim));
    rotulo.textContent = sim ? 'Salvando…' : 'Salvar';
  }

  form.addEventListener('submit', function (e) {
    e.preventDefault();
    if (enviando) return;

    var senha = campoSenha.value;
    var conf = campoConf.value;

    if (!senha)                 { mostrarErro('Digite sua nova senha.', campoSenha); return; }
    if (senha.length < MIN_SENHA) { mostrarErro('A senha precisa ter pelo menos ' + MIN_SENHA + ' caracteres.', campoSenha); return; }
    if (!conf)                  { mostrarErro('Confirme sua nova senha.', campoConf); return; }
    if (senha !== conf)         { mostrarErro('As senhas não são iguais.', campoConf); return; }

    limparErro();
    ocupado(true);

    if (USAR_SERVIDOR) {
      form.submit();                       // POST em "nova-senha"
      return;
    }

    // modo demonstração: nada é gravado de verdade
    try { sessionStorage.removeItem(CHAVE_OK); } catch (err) {}
    setTimeout(function () {
      erro.textContent = 'Senha alterada! Voltando para o login…';
      erro.classList.add('ok');
      setTimeout(function () { location.href = DESTINO_OK; }, 1200);
    }, 450);
  });

  window.addEventListener('pageshow', function (e) { if (e.persisted) ocupado(false); });

  if (window.matchMedia('(min-width: 821px)').matches) campoSenha.focus();
})();