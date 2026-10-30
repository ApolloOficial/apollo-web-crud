/* =====================================================
   APOLLO — SIDEBAR POR TIPO DE USUÁRIO
   Uso em cada tela:
     <aside class="sidebar" data-ativo="dashboard"></aside>
     <script src="sidebar.js"></script>
   data-ativo = dashboard | filial | usuarios | contrato | empresa | perfil
   data-tipo  = (opcional) tipos que podem abrir a tela: "gerente" ou "tecnico,operador".
                Quem não estiver na lista é levado para o início do próprio perfil.

   Menus:
     Técnico / Operador → Dashboard, Filial e Usuários
     Gerente            → Dashboard, Empresa e Contrato
   (Perfil fica sempre no rodapé.)

   TELAS COMPARTILHADAS (detalhes-filial, detalhes-inversor, detalhes-string,
   detalhes, membros): qualquer perfil abre, e a sidebar NÃO muda.
   O gerente que chega nelas pela Empresa continua com o menu de gerente
   (item "Empresa" marcado). Antes, essas telas trocavam o menu para o de técnico.

   De onde vem o perfil (ordem):
     1) sessionStorage 'apollo_perfil'  → gravado pelo login (manda sempre)
     2) ?perfil=gerente|tecnico|operador na URL → só para testes, e só sem login
     3) último perfil lembrado (localStorage)  → padrão: gerente

   NOME NO TOPO DIREITO: o sidebar.js também troca o nome (e as iniciais do avatar)
   de <header> ... <div class="usuario"> conforme o perfil:
     Gerente → Neymar Júnior César | Operador → Carlos Menezes | Técnico → Rafael Almeida Santos
   Para um nome fora do topo, use data-usuario-nome / data-usuario-iniciais no elemento.

   ATENÇÃO: isto é só a parte visual. O servidor precisa conferir o perfil
   em toda requisição.
   ===================================================== */
(function () {
    'use strict';

    var CHAVE = 'apollo-perfil';          // lembrado no navegador (modo teste)
    var CHAVE_LOGIN = 'apollo_perfil';    // gravado pelo login.js (sessionStorage)
    var EXIGIR_LOGIN = false;             // true = sem login, volta para o login
    var PAGINA_LOGIN = 'login.html';

    var ICONES = {
        dashboard: '<rect x="3" y="3" width="7" height="9" rx="1"/><rect x="14" y="3" width="7" height="5" rx="1"/><rect x="14" y="12" width="7" height="9" rx="1"/><rect x="3" y="16" width="7" height="5" rx="1"/>',
        filial:    '<circle cx="6" cy="5" r="2"/><circle cx="6" cy="19" r="2"/><circle cx="18" cy="8" r="2"/><path d="M6 7v10M18 10c0 4-6 3-12 7"/>',
        usuarios:  '<circle cx="9" cy="8" r="3.5"/><path d="M2.5 20c.5-4 3-6 6.5-6s6 2 6.5 6"/><circle cx="17" cy="9" r="2.5"/><path d="M17 14c2.5 0 4 1.7 4.5 5"/>',
        contrato:  '<path d="M14 3H7a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V8z"/><path d="M14 3v5h5M9 13h6M9 17h6"/>',
        empresa:   '<rect x="4" y="3" width="16" height="18" rx="1"/><path d="M9 7h2M13 7h2M9 11h2M13 11h2M9 21v-4h6v4"/>',
        perfil:    '<circle cx="12" cy="8" r="4"/><path d="M4 21c.7-4.5 4-6 8-6s7.3 1.5 8 6"/>'
    };

    var ITENS = {
        dashboard: { texto: 'Dashboard', href: 'dashboard-tecnico.html' },
        filial:    { texto: 'Filial',    href: 'detalhes-filial.html' },
        usuarios:  { texto: 'Usuários',  href: 'membros.html' },
        contrato:  { texto: 'Contrato',  href: 'contrato.html' },
        empresa:   { texto: 'Empresa',   href: 'empresa.html' },
        perfil:    { texto: 'Perfil',    href: 'perfil.html' }
    };

    var MENU = {
        tecnico:  ['dashboard', 'filial', 'usuarios'],
        operador: ['dashboard', 'filial', 'usuarios'],
        gerente:  ['dashboard', 'empresa', 'contrato']
    };

    // telas que têm uma versão para cada tipo de usuário
    var ROTAS = {
        dashboard: { gerente: 'dashboard-gerente.html' },
        perfil:    { gerente: 'perfil-gerente.html', tecnico: 'perfil-tecnico.html', operador: 'perfil-tecnico.html' }
    };

    // telas RESTRITAS: só abrem para os tipos listados (os outros voltam ao início)
    var PAGINAS = {
        'dashboard-gerente.html': ['gerente'],
        'contrato.html':          ['gerente'],
        'empresa.html':           ['gerente'],
        'perfil-gerente.html':    ['gerente'],
        'dashboard-tecnico.html': ['tecnico', 'operador'],
        'perfil-tecnico.html':    ['tecnico', 'operador']
    };
    // telas COMPARTILHADAS (qualquer perfil; a sidebar não troca): detalhes-filial,
    // detalhes-inversor, detalhes-string, detalhes, membros → não entram em PAGINAS.

    var NOMES = { tecnico: 'Técnico', operador: 'Operador', gerente: 'Gerente' };

    // Nome que aparece no topo direito de TODAS as telas, conforme o perfil.
    // (Enquanto não há servidor, os nomes são fixos; depois, vêm do login.)
    var USUARIOS = {
        gerente:  'Neymar Júnior César',
        operador: 'Carlos Menezes',
        tecnico:  'Rafael Almeida Santos'
    };
    var ALIAS = { adm: 'tecnico' };   // nome antigo

    function nomeDaPagina() {
        return decodeURIComponent(location.pathname.split('/').pop() || '').toLowerCase();
    }

    function normalizar(v) {
        v = ALIAS[v] || v;
        return MENU[v] ? v : null;
    }

    function lerLogin() {
        try { return normalizar(sessionStorage.getItem(CHAVE_LOGIN)); } catch (e) { return null; }
    }

    function lerSalvo() {
        try { return normalizar(localStorage.getItem(CHAVE)); } catch (e) { return null; }
    }

    function gravar(tipo) {
        try { localStorage.setItem(CHAVE, tipo); } catch (e) {}
    }

    // Perfil de quem está na tela. Quem fez login não consegue trocar pela URL.
    function perfilAtual() {
        var login = lerLogin();
        if (login) return login;

        var url = normalizar(new URLSearchParams(location.search).get('perfil'));
        if (url) { gravar(url); return url; }

        return lerSalvo() || 'gerente';
    }

    function inicio(tipo) {
        return (ROTAS.dashboard[tipo]) || ITENS.dashboard.href;
    }

    function permitidosDaPagina(aside) {
        var attr = aside.getAttribute('data-tipo');
        if (attr) {
            return attr.split(',').map(function (t) { return normalizar(t.trim()); })
                                  .filter(Boolean);
        }
        return PAGINAS[nomeDaPagina()] || null;      // null = compartilhada
    }

    function item(chave, ativo, extra, tipo) {
        var i = ITENS[chave];
        var href = (ROTAS[chave] && ROTAS[chave][tipo]) || i.href;
        var cls = 'menu-item' + (extra || '') + (ativo === chave ? ' ativo' : '');
        return '<a href="' + href + '" class="' + cls + '"' + (ativo === chave ? ' aria-current="page"' : '') + '>' +
               '<svg viewBox="0 0 24 24">' + ICONES[chave] + '</svg>' + i.texto + '</a>';
    }

    function bloquear(destino) {
        document.documentElement.style.visibility = 'hidden';
        if (nomeDaPagina() !== destino.toLowerCase()) location.replace(destino);
    }

    function montar() {
        var aside = document.querySelector('aside.sidebar');
        if (!aside) return;

        var tipo = perfilAtual();

        // sem login e a tela exige login → volta para o login
        if (EXIGIR_LOGIN && !lerLogin()) { bloquear(PAGINA_LOGIN); return; }

        // tela restrita a outros tipos → vai para o início do próprio perfil (não troca o perfil)
        var permitidos = permitidosDaPagina(aside);
        if (permitidos && permitidos.indexOf(tipo) < 0) { bloquear(inicio(tipo)); return; }

        // o item marcado precisa existir no menu do tipo
        // (gerente em Filial/Usuários/detalhes → "Empresa")
        var ativo = aside.getAttribute('data-ativo') || '';
        if (ativo && ativo !== 'perfil' && MENU[tipo].indexOf(ativo) < 0) {
            ativo = tipo === 'gerente' ? 'empresa' : 'filial';
        }

        document.documentElement.setAttribute('data-perfil', tipo);
        aside.setAttribute('data-perfil', tipo);
        aside.innerHTML =
            '<div class="logo"><div class="logo-apollo" role="img" aria-label="Apollo">' +
                '<div class="nome"><i class="a-anel"></i><i class="a-haste"></i><i class="p-anel"></i><i class="p-haste"></i>' +
                '<i class="o1-anel"></i><i class="l1"></i><i class="l2"></i><i class="o2-anel"></i></div>' +
                '<div class="marca-apollo"><i class="m1"></i><i class="m2"></i></div>' +
            '</div></div>' +
            '<nav class="menu">' + MENU[tipo].map(function (c) { return item(c, ativo, '', tipo); }).join('') + '</nav>' +
            item('perfil', ativo, ' menu-baixo', tipo);
    }

    /* ---------- Nome do usuário no topo direito ---------- */
    function iniciais(nome) {
        var p = nome.split(/\s+/).filter(Boolean);
        return ((p[0] || '').charAt(0) + (p[1] || '').charAt(0)).toUpperCase();
    }

    // acha o nome dentro de <header> ... <div class="usuario">:
    // marcado com data-usuario-nome / .nome-usuario, <span> sem classe, ou texto solto
    function acharNome(box) {
        var marcado = box.querySelector('[data-usuario-nome], .nome-usuario');
        if (marcado) return marcado;

        for (var i = 0; i < box.children.length; i++) {
            var c = box.children[i];
            if (c.tagName === 'SPAN' && !c.className && c.textContent.trim()) return c;
        }
        for (var j = 0; j < box.childNodes.length; j++) {
            var n = box.childNodes[j];
            if (n.nodeType === 3 && n.nodeValue.trim()) return n;
        }
        return null;
    }

    function aplicarUsuario() {
        var nome = USUARIOS[perfilAtual()];
        if (!nome) return;
        var sigla = iniciais(nome);
        var cada = function (lista, fn) { Array.prototype.forEach.call(lista, fn); };

        // marcações explícitas em qualquer lugar da tela
        cada(document.querySelectorAll('[data-usuario-nome]'), function (el) { el.textContent = nome; });
        cada(document.querySelectorAll('[data-usuario-iniciais]'), function (el) { el.textContent = sigla; });

        // topo direito
        cada(document.querySelectorAll('header .usuario'), function (box) {
            var alvo = acharNome(box);
            if (alvo) {
                if (alvo.nodeType === 3) alvo.nodeValue = alvo.nodeValue.replace(/\S[\s\S]*\S|\S/, nome);
                else alvo.textContent = nome;
            }
            // avatar com iniciais (os vazios e os com foto não mudam)
            cada(box.querySelectorAll('.avatar-usuario, .avatar, .avatar-foto'), function (a) {
                if (!a.children.length && a.textContent.trim() && !a.style.backgroundImage) a.textContent = sigla;
            });
        });
    }

    window.Apollo = {
        versao: 6,
        perfil: function () { return perfilAtual(); },
        nomePerfil: function () { return NOMES[perfilAtual()]; },
        nomeUsuario: function () { return USUARIOS[perfilAtual()]; },
        // só funciona sem login (testes); com login o perfil é o do login
        definirPerfil: function (tipo) {
            tipo = normalizar(tipo);
            if (!tipo || lerLogin()) return;
            var a = document.querySelector('aside.sidebar');
            if (a) a.removeAttribute('data-tipo');
            gravar(tipo);
            montar();
            aplicarUsuario();
        },
        sair: function () {
            try { sessionStorage.removeItem(CHAVE_LOGIN); localStorage.removeItem(CHAVE); } catch (e) {}
            location.href = PAGINA_LOGIN;
        }
    };

    montar();

    // o topo (<header>) vem depois deste script: espera a página ser lida
    if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', aplicarUsuario);
    else aplicarUsuario();
})();