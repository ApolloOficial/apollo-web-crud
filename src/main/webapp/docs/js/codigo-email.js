/* =====================================================
   APOLLO — VALIDAR CÓDIGO DO EMAIL (tela login_codigo.html)

   USAR_SERVIDOR = false -> modo demonstração (sem servidor):
       gera um código de 6 números, guarda no sessionStorage com
       validade e limite de tentativas, e mostra o código no
       CONSOLE do navegador (F12), porque nenhum email é enviado.
   USAR_SERVIDOR = true  -> valida só o formato aqui e envia o
       POST para "validar-codigo". Quem confere o código é o
       servidor. Ele pode voltar com ?erro=invalido | expirado |
       tentativas para a tela mostrar a mensagem certa.

   Também expõe ApolloCodigo.valido('123456') para usar no JS.
   ===================================================== */
(function () {
  'use strict';

  var USAR_SERVIDOR  = false;
  var TAMANHO        = 6;                  // quantidade de números do código
  var VALIDADE_MIN   = 10;                 // minutos até o código expirar (modo demonstração)
  var MAX_TENTATIVAS = 5;                  // erros permitidos (modo demonstração)
  var DESTINO_OK     = 'login_nova_senha.html';  // para onde ir quando o código estiver certo

  var MSG = {
    vazio:      'Digite o código que você recebeu.',
    curto:      'O código tem ' + TAMANHO + ' números.',
    invalido:   'Código incorreto. Confira e tente de novo.',
    expirado:   'Código expirado. Volte e peça um novo.',
    tentativas: 'Muitas tentativas. Volte e peça um novo código.'
  };

  var $ = function (id) { return document.getElementById(id); };
  var form = $('form-codigo');
  var wrap = document.querySelector('.form-wrap');
  var erro = $('erro');
  var campo = form.elements.codigo;
  var botao = form.querySelector('.entrar');
  var rotulo = botao.querySelector('.entrar-txt');
  var enviando = false;

  /* ---------- Formato do código ---------- */
  function valido(codigo) {
    return new RegExp('^\\d{' + TAMANHO + '}$').test(String(codigo || ''));
  }
  window.ApolloCodigo = { valido: valido, tamanho: TAMANHO };

  /* ---------- Email de destino (parcialmente escondido) ---------- */
  var params = new URLSearchParams(location.search);
  var email = params.get('email');

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

  /* ---------- Mensagens de erro ---------- */
  function limparErro() {
    erro.textContent = '';
    campo.removeAttribute('aria-invalid');
    campo.removeAttribute('aria-describedby');
  }

  function mostrarErro(msg, limparCampo) {
    limparErro();
    erro.textContent = msg;
    campo.setAttribute('aria-invalid', 'true');
    campo.setAttribute('aria-describedby', 'erro');
    if (limparCampo) campo.value = '';
    campo.focus();
    wrap.classList.remove('tremer');
    void wrap.offsetWidth;
    wrap.classList.add('tremer');
  }

  wrap.addEventListener('animationend', function () { wrap.classList.remove('tremer'); });

  /* ---------- Campo: só números, no máximo 6 (vale também para colar) ---------- */
  campo.maxLength = TAMANHO;
  campo.addEventListener('input', function () {
    campo.value = campo.value.replace(/\D/g, '').slice(0, TAMANHO);
    limparErro();
  });

  /* ---------- Modo demonstração: código guardado no navegador ---------- */
  var CHAVE = 'apollo-codigo:' + String(email || '').toLowerCase();

  function ler() {
    try { return JSON.parse(sessionStorage.getItem(CHAVE)); } catch (e) { return null; }
  }
  function gravar(estado) {
    try { sessionStorage.setItem(CHAVE, JSON.stringify(estado)); } catch (e) {}
  }
  function apagar() {
    try { sessionStorage.removeItem(CHAVE); } catch (e) {}
  }

  function gerarCodigo() {
    var n = new Uint32Array(1);
    window.crypto.getRandomValues(n);
    return String(n[0] % Math.pow(10, TAMANHO)).padStart(TAMANHO, '0');
  }

  function novoEstado() {
    var estado = {
      codigo: gerarCodigo(),
      expira: Date.now() + VALIDADE_MIN * 60 * 1000,
      tentativas: 0
    };
    gravar(estado);
    console.info('[Apollo] Código de teste para ' + (email || '(sem email)') + ': ' + estado.codigo);
    return estado;
  }

  // retorna { ok: true } ou { ok: false, motivo, restantes }
  function conferirLocal(codigo) {
    var estado = ler();
    if (!estado) return { ok: false, motivo: 'expirado' };

    if (Date.now() > estado.expira) {
      apagar();
      return { ok: false, motivo: 'expirado' };
    }
    if (estado.tentativas >= MAX_TENTATIVAS) return { ok: false, motivo: 'tentativas' };

    if (codigo === estado.codigo) {
      apagar();
      return { ok: true };
    }

    estado.tentativas += 1;
    gravar(estado);
    if (estado.tentativas >= MAX_TENTATIVAS) return { ok: false, motivo: 'tentativas' };
    return { ok: false, motivo: 'invalido', restantes: MAX_TENTATIVAS - estado.tentativas };
  }

  // Caixa visível com o código de teste (só no modo demonstração)
  function mostrarCodigoTeste(codigo) {
    var caixa = $('codigo-teste');
    if (!caixa) {
      caixa = document.createElement('div');
      caixa.id = 'codigo-teste';
      caixa.style.cssText =
        'margin:0 0 18px;padding:12px 14px;border:1px dashed var(--laranja);' +
        'border-radius:8px;background:rgba(255,107,53,.08);font-size:.8rem;' +
        'line-height:1.5;text-align:center;color:var(--texto)';
      form.parentNode.insertBefore(caixa, form);
    }
    caixa.innerHTML =
      'Modo teste: nenhum email foi enviado.<br>Seu código é ' +
      '<strong style="font-size:1.1rem;letter-spacing:.2em">' + codigo + '</strong><br>' +
      '<button type="button" id="preencher-codigo" ' +
      'style="margin-top:8px;padding:5px 14px;border:1px solid var(--laranja);border-radius:999px;' +
      'background:#fff;color:var(--texto);font:inherit;font-size:.78rem;font-weight:600;cursor:pointer">' +
      'Preencher</button>';
    $('preencher-codigo').addEventListener('click', function () {
      campo.value = codigo;
      limparErro();
      campo.focus();
    });
  }

  if (!USAR_SERVIDOR) {
    var atual = ler();
    if (!atual || Date.now() > atual.expira) atual = novoEstado();
    mostrarCodigoTeste(atual.codigo);
  }

  // erro devolvido pelo servidor (?erro=invalido | expirado | tentativas)
  var erroUrl = params.get('erro');
  if (erroUrl && MSG[erroUrl]) {
    mostrarErro(MSG[erroUrl], erroUrl === 'invalido');
  }

  /* ---------- Envio ---------- */
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
    if (!codigo)         { mostrarErro(MSG.vazio); return; }
    if (!valido(codigo)) { mostrarErro(MSG.curto); return; }

    limparErro();
    ocupado(true);

    if (USAR_SERVIDOR) {
      form.submit();                       // POST em "validar-codigo"
      return;
    }

    setTimeout(function () {
      var r = conferirLocal(codigo);
      if (r.ok) {
        location.href = DESTINO_OK + (email ? '?email=' + encodeURIComponent(email) : '');
        return;
      }
      ocupado(false);
      var msg = MSG[r.motivo];
      if (r.motivo === 'invalido' && r.restantes) {
        msg += ' (' + r.restantes + (r.restantes === 1 ? ' tentativa restante)' : ' tentativas restantes)');
      }
      mostrarErro(msg, r.motivo === 'invalido');
    }, 450);
  });

  // voltar pelo botão "voltar" do navegador não pode deixar o botão travado
  window.addEventListener('pageshow', function (e) { if (e.persisted) ocupado(false); });

  if (window.matchMedia('(min-width: 821px)').matches) campo.focus();
})();