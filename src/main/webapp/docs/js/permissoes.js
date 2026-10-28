/* =====================================================
   APOLLO — PERMISSÕES POR PERFIL
   O perfil vem do sidebar.js (Apollo.perfil()), que lê o login.
   Sem o sidebar.js na página, vale o perfil mais restrito (operador).

   QUEM CADASTRA QUEM
     gerente  -> cadastra técnico
     tecnico  -> cadastra usuário (operador)
     operador -> não cadastra ninguém

   QUEM MEXE EM QUÊ (mude aqui, em PODE, se as regras forem outras)
     filial       = editar / inativar / excluir a filial
     equipamento  = cadastrar, editar, inativar e excluir inversor, string e painel
     gerente  -> filial + equipamento
     tecnico  -> equipamento
     operador -> somente leitura (manutenções continuam liberadas)

   COMO USAR NO HTML
     <button data-permissao="filial" ...>        some se o perfil não pode
     <button data-permissao="equipamento" ...>   (o pop-up ligado em data-abrir sai junto)

   ATENÇÃO: esconder botão é só visual. O servidor precisa
   conferir o perfil de novo antes de gravar qualquer coisa.
   ===================================================== */
(function () {
    'use strict';

    var perfil = (window.Apollo && window.Apollo.perfil()) || 'operador';

    var PODE = {
        gerente:  { filial: true,  equipamento: true  },
        tecnico:  { filial: false, equipamento: true  },
        operador: { filial: false, equipamento: false }
    };

    var CADASTRA = {
        gerente:  ['Técnico'],
        tecnico:  ['Operador'],
        operador: []
    };

    var ROTULO_BOTAO = {
        gerente: 'Cadastrar técnico',
        tecnico: 'Cadastrar usuário'
    };

    var CLASSE_CARGO = {
        'Técnico':  'cargo-azul',
        'Operador': 'cargo-amarelo'
    };

    var NOMES = { gerente: 'Gerente', tecnico: 'Técnico', operador: 'Operador' };

    var direitos = PODE[perfil] || PODE.operador;
    var cargos = CADASTRA[perfil] || [];

    function pode(chave) { return !!direitos[chave]; }

    // para o CSS (ex.: esconder a coluna "Ações" da tabela de painéis)
    document.documentElement.setAttribute('data-perfil', perfil);
    document.documentElement.setAttribute('data-pode-equipamento', pode('equipamento') ? 'sim' : 'nao');
    document.documentElement.setAttribute('data-pode-filial', pode('filial') ? 'sim' : 'nao');


    /* ---------- Remove o que o perfil não pode usar ---------- */
    function aplicar() {
        document.querySelectorAll('[data-permissao]').forEach(function (el) {
            if (pode(el.getAttribute('data-permissao'))) return;

            var alvo = el.getAttribute('data-abrir');
            var dialog = alvo ? document.getElementById(alvo) : null;
            if (dialog) dialog.remove();

            el.remove();
        });

        // cabeçalho sem nenhum botão → avisa que a tela está só para leitura
        document.querySelectorAll('.acoes').forEach(function (acoes) {
            if (acoes.children.length > 0) return;

            var nome = acoes.closest('.card-inversor');
            acoes.remove();

            var linha = nome && nome.querySelector('.linha-nome');
            if (linha && !linha.querySelector('.selo-leitura')) {
                var selo = document.createElement('span');
                selo.className = 'selo-leitura';
                selo.textContent = 'Somente leitura';
                linha.appendChild(selo);
            }
        });
    }

    aplicar();
    // linhas da tabela e cards são desenhados depois pelo JS da tela
    new MutationObserver(aplicar).observe(document.body, { childList: true, subtree: true });


    /* ---------- Breadcrumb: o primeiro item depende do perfil ---------- */
    document.querySelectorAll('.breadcrumb a[href="empresa.html"]').forEach(function (a) {
        if (perfil === 'gerente') {
            a.textContent = 'Empresa';
            return;
        }
        // técnico e operador não têm a tela Empresa
        var sep = a.nextElementSibling;
        if (sep && sep.classList.contains('sep')) sep.remove();
        a.remove();
    });


    /* ---------- Perfil ao lado do nome ---------- */
    var usuario = document.querySelector('.usuario');
    var nomeEl = usuario && usuario.querySelector('span:not(.seta)');
    if (nomeEl && !usuario.querySelector('.selo-perfil')) {
        var selo = document.createElement('small');
        selo.className = 'selo-perfil';
        selo.textContent = NOMES[perfil] || perfil;
        nomeEl.insertAdjacentElement('afterend', selo);
    }


    /* ---------- Cadastrar usuário (tela da filial / equipe) ---------- */
    var botao = document.getElementById('btnCadastrarUsuario');
    var dialogUsuario = document.getElementById('dlg-cadastrar-usuario');
    if (!botao || !dialogUsuario) return;

    // operador: não vê nada
    if (cargos.length === 0) {
        botao.remove();
        dialogUsuario.remove();
        return;
    }

    botao.textContent = ROTULO_BOTAO[perfil];
    document.getElementById('tituloCadastrarUsuario').textContent = ROTULO_BOTAO[perfil];

    var form = dialogUsuario.querySelector('form');
    var select = document.getElementById('cargoNovoUsuario');

    // com um único cargo possível ele já vem selecionado
    select.innerHTML = (cargos.length > 1 ? '<option value="" selected></option>' : '') +
        cargos.map(function (c, i) {
            return '<option' + (cargos.length === 1 && i === 0 ? ' selected' : '') + '>' + c + '</option>';
        }).join('');

    function escapar(texto) {
        var div = document.createElement('div');
        div.textContent = texto;
        return div.innerHTML;
    }

    function iniciais(nome) {
        var partes = nome.trim().split(/\s+/);
        var a = partes[0].charAt(0);
        var b = partes.length > 1 ? partes[partes.length - 1].charAt(0) : '';
        return (a + b).toUpperCase();
    }

    form.addEventListener('submit', function (e) {
        if (e.defaultPrevented) return;                        // faltou campo / e-mail inválido
        if (!e.submitter || e.submitter.value !== 'ok') return;

        var f = form.elements;
        var cargo = f.cargo.value;

        // confere de novo (alguém pode ter mexido no HTML)
        if (cargos.indexOf(cargo) === -1) {
            e.preventDefault();
            return;
        }

        var nome = f.nome.value.trim();
        var email = f.email.value.trim();

        var li = document.createElement('li');
        li.innerHTML =
            '<div class="avatar-mini">' + escapar(iniciais(nome)) + '</div>' +
            '<div>' +
                '<span class="membro-nome">' + escapar(nome) + ' <span class="cargo ' + CLASSE_CARGO[cargo] + '">' + escapar(cargo) + '</span></span>' +
                '<span class="membro-email">' + escapar(email) + '</span>' +
            '</div>';

        document.querySelector('.lista-equipe').appendChild(li);
    });
})();