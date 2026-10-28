/* =====================================================
   APOLLO — PERMISSÕES POR PERFIL
   Quem pode cadastrar quem:
     gerente  -> cadastra técnico
     tecnico  -> cadastra usuário (operador)
     operador -> não cadastra ninguém

   O perfil vem do login (login.js guarda em sessionStorage).
   Para testar sem login, abra a tela com ?perfil=operador,
   ?perfil=tecnico ou ?perfil=gerente.

   ATENÇÃO: esconder o botão aqui é só visual. O servidor
   precisa conferir o perfil de novo antes de cadastrar.
   ===================================================== */
(function () {
    'use strict';

    let CHAVE = 'apollo_perfil';

    let PODE_CADASTRAR = {
        gerente:  ['Técnico'],
        tecnico:  ['Operador'],
        operador: []
    };

    let ROTULO_BOTAO = {
        gerente: 'Cadastrar técnico',
        tecnico: 'Cadastrar usuário'
    };

    let CLASSE_CARGO = {
        'Técnico':  'cargo-azul',
        'Operador': 'cargo-amarelo'
    };

    /* ---------- Perfil atual ---------- */
    function perfilAtual() {
        let url = new URLSearchParams(window.location.search).get('perfil');
        try {
            if (url && PODE_CADASTRAR.hasOwnProperty(url)) sessionStorage.setItem(CHAVE, url);
            let salvo = sessionStorage.getItem(CHAVE);
            if (salvo && PODE_CADASTRAR.hasOwnProperty(salvo)) return salvo;
        } catch (erro) { /* sessionStorage indisponível */ }

        return (url && PODE_CADASTRAR.hasOwnProperty(url)) ? url : 'gerente';
    }

    let perfil = perfilAtual();
    let cargos = PODE_CADASTRAR[perfil];

    let botao = document.getElementById('btnCadastrarUsuario');
    let dialog = document.getElementById('dlg-cadastrar-usuario');
    if (!botao || !dialog) return;

    /* ---------- Operador: não vê nada ---------- */
    if (cargos.length === 0) {
        botao.remove();
        dialog.remove();
        return;
    }

    /* ---------- Técnico / Gerente ---------- */
    botao.textContent = ROTULO_BOTAO[perfil];
    document.getElementById('tituloCadastrarUsuario').textContent = ROTULO_BOTAO[perfil];

    let form = dialog.querySelector('form');
    let select = document.getElementById('cargoNovoUsuario');

    // com um único cargo possível ele já vem selecionado
    select.innerHTML = (cargos.length > 1 ? '<option value="" selected></option>' : '') +
        cargos.map(function (c, i) {
            return '<option' + (cargos.length === 1 && i === 0 ? ' selected' : '') + '>' + c + '</option>';
        }).join('');

    /* ---------- Cadastrar na lista da equipe ---------- */
    function escapar(texto) {
        let div = document.createElement('div');
        div.textContent = texto;
        return div.innerHTML;
    }

    function iniciais(nome) {
        let partes = nome.trim().split(/\s+/);
        let a = partes[0].charAt(0);
        let b = partes.length > 1 ? partes[partes.length - 1].charAt(0) : '';
        return (a + b).toUpperCase();
    }

    form.addEventListener('submit', function (e) {
        if (e.defaultPrevented) return;                        // faltou campo
        if (!e.submitter || e.submitter.value !== 'ok') return;

        let f = form.elements;
        let cargo = f.cargo.value;

        // confere de novo (alguém pode ter mexido no HTML)
        if (cargos.indexOf(cargo) === -1) {
            e.preventDefault();
            return;
        }

        let nome = f.nome.value.trim();
        let email = f.email.value.trim();

        let li = document.createElement('li');
        li.innerHTML =
            '<div class="avatar-mini">' + escapar(iniciais(nome)) + '</div>' +
            '<div>' +
                '<span class="membro-nome">' + escapar(nome) + ' <span class="cargo ' + CLASSE_CARGO[cargo] + '">' + escapar(cargo) + '</span></span>' +
                '<span class="membro-email">' + escapar(email) + '</span>' +
            '</div>';

        document.querySelector('.lista-equipe').appendChild(li);
    });
})();