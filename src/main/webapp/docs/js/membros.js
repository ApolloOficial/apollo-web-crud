(function () {
    'use strict';

    /* ---------- Dados (iguais à imagem: 12 membros) ---------- */
    var FILIAIS = ['Solaris Osasco', 'Solaris Campinas', 'Solaris Santos'];
    var DOM = '@solarisindustria.com.br';
    var membros = [
        { n: 'Ana Souza',       f: FILIAIS[0], c: 'Técnico',  e: 'ana.souza' + DOM,       a: true },
        { n: 'Bruno Costa',     f: FILIAIS[1], c: 'Operador', e: 'bruno.costa' + DOM,     a: true },
        { n: 'Diego Lima',      f: FILIAIS[0], c: 'Operador', e: 'diego.lima' + DOM,      a: true },
        { n: 'Elaine Rocha',    f: FILIAIS[1], c: 'Técnico',  e: 'elaine.rocha' + DOM,    a: false },
        { n: 'Marcos Oliveira', f: FILIAIS[1], c: 'Operador', e: 'marcos.oliveira' + DOM, a: true },
        { n: 'Júlia Ferreira',  f: FILIAIS[1], c: 'Operador', e: 'julia.ferreira' + DOM,  a: true },
        { n: 'Rafael Mendes',   f: FILIAIS[0], c: 'Técnico',  e: 'rafael.mendes' + DOM,   a: true },
        { n: 'Paula Nunes',     f: FILIAIS[2], c: 'Técnico',  e: 'paula.nunes' + DOM,     a: true },
        { n: 'Lucas Barbosa',   f: FILIAIS[2], c: 'Operador', e: 'lucas.barbosa' + DOM,   a: true },
        { n: 'Camila Duarte',   f: FILIAIS[0], c: 'Operador', e: 'camila.duarte' + DOM,   a: true },
        { n: 'Tiago Almeida',   f: FILIAIS[2], c: 'Operador', e: 'tiago.almeida' + DOM,   a: true },
        { n: 'Fernanda Lopes',  f: FILIAIS[1], c: 'Operador', e: 'fernanda.lopes' + DOM,  a: true }
    ];
    var POR_PAGINA = 6;
    var st = { grupo: 'todos', filial: '', busca: '', ordem: '', pagina: 1 };
    var editando = null;

    var $ = function (id) { return document.getElementById(id); };
    var corpo = $('corpo'), menu = $('menu'), dlg = $('dlg-membro'), form = $('form-membro');
    var menuDono = null;

    /* ---------- Permissões: gerente cadastra técnico, técnico cadastra operador, operador não cadastra ---------- */
    var perfil = (window.Apollo && window.Apollo.perfil()) || 'operador';   // sem sidebar.js: o mais restrito
    var CADASTRA = { gerente: ['Técnico'], tecnico: ['Operador'], operador: [] };
    var cargos = CADASTRA[perfil] || [];
    var temAcoes = cargos.length > 0;
    function podeGerir(m) { return cargos.indexOf(m.c) > -1; }   // só mexe em quem pode cadastrar

    function esc(t) { var d = document.createElement('div'); d.textContent = t; return d.innerHTML; }
    function iniciais(n) { var p = n.trim().split(/\s+/); return (p[0][0] + (p.length > 1 ? p[p.length - 1][0] : '')).toUpperCase(); }

    /* ---------- Filtro ---------- */
    function filtrados() {
        var q = st.busca.toLowerCase().trim();
        var r = membros.filter(function (m) {
            if (st.grupo === 'tecnico' && m.c !== 'Técnico') return false;
            if (st.grupo === 'operador' && m.c !== 'Operador') return false;
            if (st.grupo === 'inativo' && m.a) return false;
            if (st.filial && m.f !== st.filial) return false;
            return !q || (m.n + ' ' + m.e).toLowerCase().indexOf(q) > -1;
        });
        if (st.ordem) r.sort(function (a, b) { return a.n.localeCompare(b.n, 'pt-BR') * (st.ordem === 'az' ? 1 : -1); });
        return r;
    }

    /* ---------- Render ---------- */
    function renderResumo() {
        var tec = membros.filter(function (m) { return m.c === 'Técnico'; }).length;
        var ope = membros.filter(function (m) { return m.c === 'Operador'; }).length;
        var ina = membros.filter(function (m) { return !m.a; }).length;
        var fil = {}; membros.forEach(function (m) { fil[m.f] = 1; });

        $('st-total').textContent = membros.length;
        $('st-tec').textContent = tec;
        $('st-ope').textContent = ope;
        $('st-fil').textContent = Object.keys(fil).length;

        var chips = [['todos', 'Todos (' + membros.length + ')'], ['tecnico', 'Técnicos (' + tec + ')'],
                     ['operador', 'Operadores (' + ope + ')'], ['inativo', 'Inativos (' + ina + ')']];
        $('chips').innerHTML = chips.map(function (c) {
            return '<button type="button" class="chip' + (st.grupo === c[0] ? ' ativa' : '') + '" data-g="' + c[0] + '" aria-pressed="' + (st.grupo === c[0]) + '">' + c[1] + '</button>';
        }).join('');
    }

    function render() {
        var lista = filtrados();
        var paginas = Math.max(1, Math.ceil(lista.length / POR_PAGINA));
        if (st.pagina > paginas) st.pagina = paginas;
        var ini = (st.pagina - 1) * POR_PAGINA;
        var fatia = lista.slice(ini, ini + POR_PAGINA);

        corpo.innerHTML = fatia.length ? fatia.map(function (m) {
            return '<tr>' +
                '<td><div class="membro"><span class="av">' + esc(iniciais(m.n)) + '</span>' + esc(m.n) + '</div></td>' +
                '<td>' + esc(m.f) + '</td><td>' + esc(m.c) + '</td><td>' + esc(m.e) + '</td>' +
                '<td><span class="selo ' + (m.a ? 'ativo' : 'inativo') + '">' + (m.a ? 'Ativo' : 'Inativo') + '</span></td>' +
                (temAcoes ? '<td>' + (podeGerir(m) ? '<button type="button" class="mais" data-i="' + membros.indexOf(m) + '" aria-haspopup="true" aria-label="Ações de ' + esc(m.n) + '">&middot;&middot;&middot;</button>' : '') + '</td>' : '') +
                '</tr>';
        }).join('') : '<tr><td colspan="' + (temAcoes ? 6 : 5) + '" class="vazio">Nenhum membro encontrado.</td></tr>';

        $('contagem').textContent = lista.length
            ? 'Mostrando ' + (ini + 1) + '–' + (ini + fatia.length) + ' de ' + lista.length + ' membros'
            : 'Mostrando 0 membros';

        var b = '<button type="button" data-p="' + (st.pagina - 1) + '" aria-label="Anterior"' + (st.pagina === 1 ? ' disabled' : '') + '>‹</button>';
        for (var p = 1; p <= paginas; p++) {
            b += '<button type="button" data-p="' + p + '"' + (p === st.pagina ? ' class="atual" aria-current="page"' : '') + '>' + p + '</button>';
        }
        b += '<button type="button" data-p="' + (st.pagina + 1) + '" aria-label="Próxima"' + (st.pagina === paginas ? ' disabled' : '') + '>›</button>';
        $('pag').innerHTML = b;

        renderResumo();
    }

    /* ---------- Menu flutuante (Filtros e Ações) ---------- */
    function fecharMenu() { menu.hidden = true; menuDono = null; }

    function abrirMenu(btn, itens) {
        if (menuDono === btn) { fecharMenu(); return; }
        menu.innerHTML = '';
        itens.forEach(function (it) {
            var b = document.createElement('button');
            b.type = 'button'; b.setAttribute('role', 'menuitem'); b.textContent = it.t;
            if (it.marcado) b.className = 'marcado';
            b.addEventListener('click', function () { fecharMenu(); it.fn(); });
            menu.appendChild(b);
        });
        menu.hidden = false;
        var r = btn.getBoundingClientRect();
        menu.style.top = (r.bottom + 4) + 'px';
        menu.style.left = Math.max(8, Math.min(r.left, window.innerWidth - menu.offsetWidth - 8)) + 'px';
        if (btn.classList.contains('mais')) menu.style.left = Math.max(8, r.right - menu.offsetWidth) + 'px';
        menuDono = btn;
    }

    document.addEventListener('click', function (e) {
        if (!e.target.closest('#menu') && !e.target.closest('#btn-filtros') && !e.target.closest('.mais')) fecharMenu();
    });
    document.addEventListener('keydown', function (e) { if (e.key === 'Escape') fecharMenu(); });
    window.addEventListener('scroll', fecharMenu, true);
    window.addEventListener('resize', fecharMenu);

    $('btn-filtros').addEventListener('click', function () {
        var op = [['', 'Ordem padrão'], ['az', 'Nome (A–Z)'], ['za', 'Nome (Z–A)']];
        abrirMenu(this, op.map(function (o) {
            return { t: o[1], marcado: st.ordem === o[0], fn: function () { st.ordem = o[0]; st.pagina = 1; render(); } };
        }));
    });

    corpo.addEventListener('click', function (e) {
        var btn = e.target.closest('.mais');
        if (!btn) return;
        var m = membros[Number(btn.getAttribute('data-i'))];
        abrirMenu(btn, [
            { t: 'Editar membro', fn: function () { abrirForm(m); } },
            { t: m.a ? 'Inativar membro' : 'Ativar membro', fn: function () { m.a = !m.a; render(); } }
        ]);
    });

    /* ---------- Controles ---------- */
    $('sel-filial').innerHTML = '<option value="">Todas as filiais</option>' + FILIAIS.map(function (f) { return '<option>' + f + '</option>'; }).join('');

    $('sel-filial').addEventListener('change', function () { st.filial = this.value; st.pagina = 1; render(); });
    $('busca').addEventListener('input', function () { st.busca = this.value; st.pagina = 1; render(); });
    $('chips').addEventListener('click', function (e) {
        var c = e.target.closest('[data-g]');
        if (c) { st.grupo = c.getAttribute('data-g'); st.pagina = 1; render(); }
    });
    $('pag').addEventListener('click', function (e) {
        var b = e.target.closest('[data-p]');
        if (b && !b.disabled) { st.pagina = Number(b.getAttribute('data-p')); render(); }
    });

    /* ---------- Máscaras e validação do formulário ---------- */
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
        var p = input.closest('.pop-campo');
        if (!p) return;
        p.classList.add('invalido');
        var er = p.querySelector('.pop-erro');
        if (!er) { er = document.createElement('small'); er.className = 'pop-erro'; p.appendChild(er); }
        er.textContent = msg;
    }

    // true se CPF, telefone e senha estão certos (o "Preencha o campo" é conferido antes)
    function dadosValidos() {
        var f = form.elements, primeiro = null;
        function falha(campo, msg) { erroNoCampo(campo, msg); if (!primeiro) primeiro = campo; }

        if (f.cpf.value.trim() && !cpfValido(f.cpf.value)) falha(f.cpf, 'CPF inválido');
        var tel = f.telefone.value.replace(/\D/g, '');
        if (tel.length > 0 && tel.length < 10) falha(f.telefone, 'Telefone inválido');
        if (!editando && f.senha.value.length < MIN_SENHA) falha(f.senha, 'A senha precisa ter pelo menos ' + MIN_SENHA + ' caracteres');

        if (primeiro) primeiro.focus();
        return !primeiro;
    }

    form.elements.cpf.addEventListener('input', function () { this.value = mascaraCpf(this.value); });
    form.elements.telefone.addEventListener('input', function () { this.value = mascaraTelefone(this.value); });

    /* ---------- Pop-up cadastrar / editar ---------- */
    function limparErros() {
        form.querySelectorAll('.pop-campo.invalido').forEach(function (c) { c.classList.remove('invalido'); });
        form.querySelectorAll('.pop-erro').forEach(function (e) { e.remove(); });
    }

    function abrirForm(m) {
        editando = m || null;
        var f = form.elements;
        $('dlg-membro-t').textContent = m ? 'Editar membro' : 'Cadastrar membro';
        $('dlg-salvar').textContent = m ? 'Salvar' : 'Cadastrar';
        $('bloco-acesso').hidden = !!m;            // senha só no cadastro
        f.senha.required = !m;
        f.cpf.required = !m;                       // membros antigos ainda não têm CPF
        $('rot-cpf').textContent = m ? 'CPF' : 'CPF *';
        if (m) {
            f.nome.value = m.n; f.email.value = m.e; f.cargo.value = m.c;
            f.telefone.value = m.t || ''; f.cpf.value = m.cpf || '';
        }
        dlg.showModal();
    }

    $('btn-novo').addEventListener('click', function () { abrirForm(null); });
    $('dlg-cancelar').addEventListener('click', function () { dlg.close(); });
    dlg.addEventListener('mousedown', function (e) { if (e.target === dlg) dlg.close(); });
    dlg.addEventListener('close', function () { form.reset(); limparErros(); editando = null; });

    form.addEventListener('input', function (e) {
        var p = e.target.closest('.pop-campo');
        if (p && e.target.value.trim()) { p.classList.remove('invalido'); var er = p.querySelector('.pop-erro'); if (er) er.remove(); }
    });

    form.addEventListener('submit', function (e) {
        var jaBarrado = e.defaultPrevented;   // e-mail inválido (validar-email.js)
        e.preventDefault();
        var primeiro = null;
        form.querySelectorAll('[required]').forEach(function (c) {
            var p = c.closest('.pop-campo'), er = p.querySelector('.pop-erro');
            var vazio = !c.value.trim();
            p.classList.toggle('invalido', vazio);
            if (vazio && !er) { er = document.createElement('small'); er.className = 'pop-erro'; er.textContent = 'Preencha o campo'; p.appendChild(er); }
            if (!vazio && er) er.remove();
            if (vazio && !primeiro) primeiro = c;
        });
        if (primeiro) { primeiro.focus(); return; }
        if (jaBarrado) return;
        if (!dadosValidos()) return;

        var f = form.elements;
        if (cargos.indexOf(f.cargo.value) < 0) return;   // confere o cargo de novo
        // sem campo de filial/status no formulário: novo membro entra ativo, na filial filtrada (ou na primeira)
        var dados = {
            n: f.nome.value.trim(), e: f.email.value.trim(), c: f.cargo.value,
            t: f.telefone.value.trim(), cpf: f.cpf.value.trim(),
            f: editando ? editando.f : (st.filial || FILIAIS[0]),
            a: editando ? editando.a : true
        };   // a senha não é guardada aqui: quem grava é o servidor

        if (editando) {
            Object.assign(editando, dados);
        } else {
            membros.push(dados);
            st.grupo = 'todos'; st.filial = ''; st.busca = ''; $('busca').value = ''; $('sel-filial').value = '';
            st.pagina = Math.ceil(membros.length / POR_PAGINA);   // vai para a página do novo membro
        }
        dlg.close();
        render();
    });

    /* ---------- Aplica o perfil na tela ---------- */
    if (!temAcoes) {
        // operador: não cadastra, não edita, não inativa
        $('btn-novo').remove();
        dlg.remove();
        document.querySelector('.tabela').classList.add('sem-acoes');
        document.querySelector('.tabela thead th:last-child').remove();
    } else {
        // só aparecem os cargos que o perfil pode cadastrar
        form.elements.cargo.innerHTML = (cargos.length > 1 ? '<option value="" selected>Selecione o perfil</option>' : '') +
            cargos.map(function (c) { return '<option' + (cargos.length === 1 ? ' selected' : '') + '>' + c + '</option>'; }).join('');
    }

    render();
})();