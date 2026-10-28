/* =====================================================
   APOLLO — SIDEBAR POR TIPO DE USUÁRIO
   Uso em cada tela:
     <aside class="sidebar" data-ativo="dashboard"></aside>
     <script src="sidebar.js"></script>
   data-ativo = dashboard | filial | usuarios | contrato | empresa | perfil
   data-tipo  = (opcional) para quais tipos a tela serve: "gerente" ou "tecnico,operador".
                Se o tipo salvo não estiver na lista, usa o primeiro da lista.
                Ex.: <aside class="sidebar" data-ativo="perfil" data-tipo="tecnico,operador"></aside>

   Tipo por página (automático, não precisa de data-tipo nessas telas):
     Gerente            → dashboard-crud, contrato, empresa, perfil-gerente
     Técnico / Operador → dashboard, detalhes-filial, detalhes, membros, perfil-tecnico
   (data-tipo, se existir, tem prioridade sobre essa lista.)

   Telas que mudam conforme o tipo:
     Dashboard → Gerente: dashboard-crud.html  | Técnico / Operador: dashboard.html
     Perfil    → Gerente: perfil-gerente.html  | Técnico / Operador: perfil-tecnico.html

   Tipos de usuário e menus:
     Técnico / Operador → Dashboard, Filial e Usuários
     Gerente            → Dashboard, Contrato e Empresa
   (Perfil fica sempre no rodapé.)

   Como o tipo é escolhido (enquanto não existe login):
     1) ?perfil=gerente | tecnico | operador na URL (fica salvo no navegador)
     2) a própria tela: abrir Filial, Usuários ou os Detalhes mostra o menu de
        Técnico/Operador; abrir Contrato ou Empresa mostra o de Gerente
     3) Dashboard e Perfil usam o último tipo salvo (padrão: gerente)
     Também dá para usar Apollo.definirPerfil('tecnico') no console.
   ===================================================== */
(function () {
    'use strict';

    var CHAVE = 'apollo-perfil';

    var ICONES = {
        dashboard: '<rect x="3" y="3" width="7" height="9" rx="1"/><rect x="14" y="3" width="7" height="5" rx="1"/><rect x="14" y="12" width="7" height="9" rx="1"/><rect x="3" y="16" width="7" height="5" rx="1"/>',
        filial:    '<circle cx="6" cy="5" r="2"/><circle cx="6" cy="19" r="2"/><circle cx="18" cy="8" r="2"/><path d="M6 7v10M18 10c0 4-6 3-12 7"/>',
        usuarios:  '<circle cx="9" cy="8" r="3.5"/><path d="M2.5 20c.5-4 3-6 6.5-6s6 2 6.5 6"/><circle cx="17" cy="9" r="2.5"/><path d="M17 14c2.5 0 4 1.7 4.5 5"/>',
        contrato:  '<path d="M12 21s-7-6.2-7-11a7 7 0 0 1 14 0c0 4.8-7 11-7 11Z"/><circle cx="12" cy="10" r="2.5"/>',
        empresa:   '<circle cx="12" cy="12" r="9"/><path d="m8.5 12.5 2.5 2.5 4.5-5"/>',
        perfil:    '<circle cx="12" cy="8" r="4"/><path d="M4 21c.7-4.5 4-6 8-6s7.3 1.5 8 6"/>'
    };

    var ITENS = {
        dashboard: { texto: 'Dashboard', href: 'dashboard.html' },   // técnico/operador (gerente: ver DASH_GERENTE)
        filial:    { texto: 'Filial',    href: 'detalhes-filial.html' },
        usuarios:  { texto: 'Usuários',  href: 'membros.html' },
        contrato:  { texto: 'Contrato',  href: 'contrato.html' },
        empresa:   { texto: 'Empresa',   href: 'empresa.html' },
        perfil:    { texto: 'Perfil',    href: 'perfil.html' }
    };

    var MENU = {
        tecnico:  ['dashboard', 'filial', 'usuarios'],
        operador: ['dashboard', 'filial', 'usuarios'],
        gerente:  ['dashboard', 'contrato', 'empresa']
    };

    // telas que têm uma versão para cada tipo de usuário
    var ROTAS = {
        dashboard: { gerente: 'dashboard-crud.html' },
        perfil:    { gerente: 'perfil-gerente.html', tecnico: 'perfil-tecnico.html', operador: 'perfil-tecnico.html' }
    };

    // cada página pertence a um grupo de tipos (usado quando a tela não tem data-tipo)
    var PAGINAS = {
        'dashboard-crud.html': ['gerente'],
        'contrato.html':       ['gerente'],
        'empresa.html':        ['gerente'],
        'perfil-gerente.html': ['gerente'],
        'dashboard.html':      ['tecnico', 'operador'],
        'detalhes-filial.html':['tecnico', 'operador'],
        'detalhes.html':       ['tecnico', 'operador'],
        'membros.html':        ['tecnico', 'operador'],
        'perfil-tecnico.html': ['tecnico', 'operador']
    };

    function permitidosDaPagina() {
        var nome = decodeURIComponent(location.pathname.split('/').pop() || '').toLowerCase();
        return PAGINAS[nome] || null;
    }

    var NOMES = { tecnico: 'Técnico', operador: 'Operador', gerente: 'Gerente' };
    var ALIAS = { adm: 'tecnico' };   // nome antigo

    function lerSalvo() {
        try { var v = localStorage.getItem(CHAVE); v = ALIAS[v] || v; return MENU[v] ? v : null; } catch (e) { return null; }
    }

    function gravar(tipo) {
        try { localStorage.setItem(CHAVE, tipo); } catch (e) {}
    }

    function perfilAtual(ativo, permitidos) {
        var url = new URLSearchParams(location.search).get('perfil');
        url = ALIAS[url] || url;

        var tipo = MENU[url] ? url : (lerSalvo() || 'gerente');   // URL > salvo > gerente

        // a tela declara para quais tipos serve (data-tipo)
        if (permitidos && permitidos.length) {
            if (permitidos.indexOf(tipo) < 0) tipo = permitidos[0];
            gravar(tipo);
            return tipo;
        }

        if (MENU[url]) { gravar(url); return url; }          // escolhido na URL

        // a tela não existe no menu deste tipo → usa o tipo a que a tela pertence
        if (ativo && ativo !== 'perfil' && MENU[tipo].indexOf(ativo) < 0) {
            ['tecnico', 'gerente'].some(function (t) {
                if (MENU[t].indexOf(ativo) > -1) { tipo = t; return true; }
            });
            gravar(tipo);
        }
        return tipo;
    }

    function item(chave, ativo, extra, tipo) {
        var i = ITENS[chave];
        var href = (ROTAS[chave] && ROTAS[chave][tipo]) || i.href;
        var cls = 'menu-item' + (extra || '') + (ativo === chave ? ' ativo' : '');
        return '<a href="' + href + '" class="' + cls + '"' + (ativo === chave ? ' aria-current="page"' : '') + '>' +
               '<svg viewBox="0 0 24 24">' + ICONES[chave] + '</svg>' + i.texto + '</a>';
    }

    function montar() {
        var aside = document.querySelector('aside.sidebar');
        if (!aside) return;

        var ativo = aside.getAttribute('data-ativo') || '';
        var attr = aside.getAttribute('data-tipo');
        var permitidos = attr ? attr.split(',').map(function (t) { t = t.trim(); return ALIAS[t] || t; })
                                          .filter(function (t) { return MENU[t]; }) : permitidosDaPagina();
        var tipo = perfilAtual(ativo, permitidos);

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

    window.Apollo = {
        versao: 4,
        perfil: function () { return perfilAtual(''); },
        nomePerfil: function () { return NOMES[perfilAtual('')]; },
        definirPerfil: function (tipo) {
            tipo = ALIAS[tipo] || tipo;
            if (!MENU[tipo]) return;
            var a = document.querySelector('aside.sidebar');
            if (a) a.removeAttribute('data-tipo');
            gravar(tipo);
            montar();
        }
    };

    montar();
})();