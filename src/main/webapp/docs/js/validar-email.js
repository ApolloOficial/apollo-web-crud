/* =====================================================
   APOLLO — VALIDAR E-MAIL
   Vale para QUALQUER formulário que tenha <input type="email">
   (login, esqueci a senha, editar empresa, editar filial,
   cadastrar usuário...). Basta incluir este arquivo na página.

   - Campo vazio: não faz nada (quem avisa é o "Preencha o campo"
     ou o próprio login).
   - E-mail inválido: bloqueia o envio e mostra
     "Informe um e-mail válido".
   - Botões com formnovalidate (Cancelar / Voltar) não são validados.

   Também expõe ApolloEmail.valido('a@b.com') para usar no JS.
   ===================================================== */
(function () {
    'use strict';

    var MENSAGEM = 'Informe um e-mail válido';

    function valido(email) {
        email = String(email || '').trim();
        if (email.length > 254) return false;

        var m = /^([^\s@]+)@([^\s@]+)$/.exec(email);
        if (!m) return false;

        var local = m[1], dominio = m[2];
        if (local.length > 64 || local.charAt(0) === '.' || local.slice(-1) === '.' || local.indexOf('..') > -1) return false;
        if (!/^[A-Za-z0-9!#$%&'*+\/=?^_`{|}~.-]+$/.test(local)) return false;

        // domínio: partes separadas por ponto, a última com 2+ letras (ex.: .com, .com.br)
        var partes = dominio.split('.');
        if (partes.length < 2) return false;
        if (!/^[A-Za-z]{2,}$/.test(partes[partes.length - 1])) return false;

        return partes.every(function (p) {
            return /^[A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?$/.test(p);
        });
    }

    window.ApolloEmail = { valido: valido, mensagem: MENSAGEM };

    function mostrar(campo) {
        var pop = campo.closest('.pop-campo');

        if (pop) {                                   // dentro de pop-up
            pop.classList.add('invalido');
            var aviso = pop.querySelector('.pop-erro');
            if (!aviso) {
                aviso = document.createElement('small');
                aviso.className = 'pop-erro';
                pop.appendChild(aviso);
            }
            aviso.textContent = MENSAGEM;
            return 'pop';
        }

        // telas de login: usa o <p id="erro">
        var erro = (campo.form && campo.form.querySelector('.erro')) || document.getElementById('erro');
        if (erro) { erro.textContent = MENSAGEM; erro.classList.add('visivel'); }
        campo.setAttribute('aria-invalid', 'true');
        return 'login';
    }

    function limpar(campo) {
        campo.removeAttribute('aria-invalid');
        var pop = campo.closest('.pop-campo');
        if (pop) {
            var aviso = pop.querySelector('.pop-erro');
            if (aviso && aviso.textContent === MENSAGEM) { pop.classList.remove('invalido'); aviso.remove(); }
            return;
        }
        var erro = (campo.form && campo.form.querySelector('.erro')) || document.getElementById('erro');
        if (erro && erro.textContent === MENSAGEM) { erro.textContent = ''; erro.classList.remove('visivel'); }
    }

    // captura no document: roda antes dos scripts de cada tela
    document.addEventListener('submit', function (e) {
        var form = e.target;
        if (e.submitter && e.submitter.hasAttribute('formnovalidate')) return;

        var invalido = null;
        form.querySelectorAll('input[type="email"]').forEach(function (campo) {
            if (campo.disabled || campo.value.trim() === '') return;
            if (!valido(campo.value)) { invalido = invalido || campo; }
        });
        if (!invalido) return;

        e.preventDefault();                          // pop-ups conferem defaultPrevented e não gravam
        var tipo = mostrar(invalido);
        if (tipo === 'login') e.stopImmediatePropagation();   // o login.js não segue com e-mail ruim
        invalido.focus();
    }, true);

    document.addEventListener('input', function (e) {
        if (e.target.matches && e.target.matches('input[type="email"]')) limpar(e.target);
    });
})();