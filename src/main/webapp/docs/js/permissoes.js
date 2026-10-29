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
     equipamento  = editar, inativar e excluir inversor, string e painel
     cadastro     = cadastrar inversor, string e painel
     manutencao   = cadastrar manutenção
     gerente  -> filial + equipamento (NÃO cadastra inversor, string nem manutenção)
     tecnico  -> equipamento + cadastro + manutencao
     operador -> somente leitura, mas cadastra manutenção

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
        gerente:  { filial: true,  equipamento: true,  cadastro: false, manutencao: false },
        tecnico:  { filial: false, equipamento: true,  cadastro: true,  manutencao: true  },
        operador: { filial: false, equipamento: false, cadastro: false, manutencao: true  }
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
    select.innerHTML = (cargos.length > 1 ? '<option value="" selected>Selecione o perfil</option>' : '') +
        cargos.map(function (c, i) {
            return '<option' + (cargos.length === 1 && i === 0 ? ' selected' : '') + '>' + c + '</option>';
        }).join('');

    /* ---------- Máscaras e validação do pop-up de cadastro ---------- */
    var MIN_SENHA = 8;

    function mascaraCpf(v) {
        var d = v.replace(/\D/g, '').slice(0, 11);
        return d.replace(/^(\d{3})(\d)/, '$1.$2')
                .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
                .replace(/\.(\d{3})(\d)/, '.$1-$2');
    }

    function mascaraTelefone(v) {
        var d = v.replace(/\D/g, '').slice(0, 11);
        if (d.length > 10) return d.replace(/^(\d{2})(\d{5})(\d{0,4}).*/, '($1) $2-$3');
        if (d.length > 6)  return d.replace(/^(\d{2})(\d{4})(\d{0,4}).*/, '($1) $2-$3');
        if (d.length > 2)  return d.replace(/^(\d{2})(\d{0,5})/, '($1) $2');
        if (d.length > 0)  return '(' + d;
        return '';
    }

    function cpfValido(v) {
        var d = v.replace(/\D/g, '');
        if (d.length !== 11 || /^(\d)\1{10}$/.test(d)) return false;
        for (var t = 9; t < 11; t++) {
            var soma = 0;
            for (var i = 0; i < t; i++) soma += Number(d.charAt(i)) * (t + 1 - i);
            if (((soma * 10) % 11) % 10 !== Number(d.charAt(t))) return false;
        }
        return true;
    }

    function erroNoCampo(input, msg) {
        var pai = input.closest('.pop-campo');
        if (!pai) return;
        pai.classList.add('invalido');
        var aviso = pai.querySelector('.pop-erro');
        if (!aviso) {
            aviso = document.createElement('small');
            aviso.className = 'pop-erro';
            pai.appendChild(aviso);
        }
        aviso.textContent = msg;
    }

    // devolve true se está tudo certo (o "Preencha o campo" é do pop-ups.js)
    function dadosValidos() {
        var f = form.elements;
        var primeiro = null;

        function falha(campo, msg) {
            erroNoCampo(campo, msg);
            if (!primeiro) primeiro = campo;
        }

        if (!cpfValido(f.cpf.value)) falha(f.cpf, 'CPF inválido');
        if (f.telefone.value.replace(/\D/g, '').length > 0 && f.telefone.value.replace(/\D/g, '').length < 10) falha(f.telefone, 'Telefone inválido');
        if (f.senha.value.length < MIN_SENHA) falha(f.senha, 'A senha precisa ter pelo menos ' + MIN_SENHA + ' caracteres');

        if (primeiro) primeiro.focus();
        return !primeiro;
    }

    form.elements.cpf.addEventListener('input', function () {
        this.value = mascaraCpf(this.value);
    });
    form.elements.telefone.addEventListener('input', function () {
        this.value = mascaraTelefone(this.value);
    });

    // Primeiro acesso (modo demonstração): quem acabou de ser cadastrado
    // precisa criar uma senha nova no primeiro login. O login.js confere esta lista.
    function marcarPrimeiroAcesso(email) {
        try {
            var lista = JSON.parse(localStorage.getItem('apollo-primeiro-acesso')) || [];
            email = String(email || '').toLowerCase();
            if (email && lista.indexOf(email) < 0) lista.push(email);
            localStorage.setItem('apollo-primeiro-acesso', JSON.stringify(lista));
        } catch (e) {}
    }

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

        // CPF, telefone e senha
        if (!dadosValidos()) {
            e.preventDefault();
            return;
        }

        var f = form.elements;
        var cargo = f.cargo.value;

        // confere de novo (alguém pode ter mexido no HTML)
        if (cargos.indexOf(cargo) === -1) {
            e.preventDefault();
            return;
        }

        var nome = f.nome.value.trim();
        var email = f.email.value.trim();
        marcarPrimeiroAcesso(email);

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