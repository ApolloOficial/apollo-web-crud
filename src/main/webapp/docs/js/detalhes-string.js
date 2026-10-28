/* ################ pop-ups.js (abrir, fechar e validar pop-ups) ################ */
/* =====================================================
   APOLLO — POP-UPS (<dialog class="pop">)

   Como usar:
   - Botão que abre:   <button data-abrir="id-do-dialog">
   - Cancelar/Voltar:  <button value="cancel" formnovalidate>
   - Campos obrigatórios: atributo "required" (mostra
     "Preencha o campo" em vermelho, igual à tela do inversor)
   ===================================================== */

(function () {
    'use strict';

    function limparErros(dialog) {
        dialog.querySelectorAll('.pop-campo.invalido').forEach(function (c) {
            c.classList.remove('invalido');
        });
        dialog.querySelectorAll('.pop-erro').forEach(function (e) {
            e.remove();
        });
    }

    function marcarErro(campo) {
        campo.classList.add('invalido');
        if (!campo.querySelector('.pop-erro')) {
            var aviso = document.createElement('small');
            aviso.className = 'pop-erro';
            aviso.textContent = 'Preencha o campo';
            campo.appendChild(aviso);
        }
    }

    function tirarErro(campo) {
        campo.classList.remove('invalido');
        var aviso = campo.querySelector('.pop-erro');
        if (aviso) aviso.remove();
    }

    /* ---------- Abrir ---------- */
    document.querySelectorAll('[data-abrir]').forEach(function (botao) {
        botao.addEventListener('click', function () {
            var dialog = document.getElementById(botao.getAttribute('data-abrir'));
            if (dialog && typeof dialog.showModal === 'function') {
                dialog.showModal();
            }
        });
    });

    /* ---------- Cada pop-up ---------- */
    document.querySelectorAll('dialog.pop').forEach(function (dialog) {
        var form = dialog.querySelector('form');

        // fecha ao clicar fora (no fundo escuro)
        dialog.addEventListener('mousedown', function (e) {
            if (e.target === dialog) dialog.close('cancel');
        });

        // volta o formulário ao estado original ao fechar
        dialog.addEventListener('close', function () {
            if (form) form.reset();
            limparErros(dialog);
        });

        if (!form) return;

        // validação própria (mensagem padronizada)
        form.noValidate = true;

        form.querySelectorAll('input, select, textarea').forEach(function (campo) {
            var evento = campo.tagName === 'SELECT' ? 'change' : 'input';
            campo.addEventListener(evento, function () {
                if (campo.value.trim() !== '') {
                    var pai = campo.closest('.pop-campo');
                    if (pai) tirarErro(pai);
                }
            });
        });

        form.addEventListener('submit', function (e) {
            var botao = e.submitter;

            // Cancelar / Voltar não valida
            if (botao && botao.hasAttribute('formnovalidate')) return;

            var primeiro = null;

            form.querySelectorAll('[required]').forEach(function (campo) {
                var pai = campo.closest('.pop-campo');
                if (!pai) return;

                if (campo.value.trim() === '') {
                    marcarErro(pai);
                    if (!primeiro) primeiro = campo;
                } else {
                    tirarErro(pai);
                }
            });

            if (primeiro) {
                e.preventDefault();
                primeiro.focus();
            }
        });
    });

    /* ---------- Inativar / Ativar string ---------- */
    var dlgInativar = document.getElementById('dlg-inativar-string');

    if (dlgInativar) {
        dlgInativar.addEventListener('close', function () {
            if (dlgInativar.returnValue !== 'ok') return;

            var botao = document.querySelector('[data-abrir="dlg-inativar-string"]');
            var ativo = botao.textContent.trim() === 'Inativar';
            var texto = ativo ? 'Inativo' : 'Ativo';

            document.querySelectorAll('[data-status-string]').forEach(function (b) {
                b.textContent = texto;
                b.classList.remove('badge-ativo', 'badge-inativo');
                b.classList.add(ativo ? 'badge-inativo' : 'badge-ativo');
            });

            botao.textContent = ativo ? 'Ativar' : 'Inativar';
        });
    }
})();

/* ################ Tela: String ################ */
/* =====================================================
   APOLLO — DETALHES DA STRING
   Usa o pop-ups.js (acima) para abrir/fechar/validar.
   Aqui: tabela de painéis (busca, filtro de status,
   paginação, adicionar, editar, métricas) e manutenções
   (cadastrar, atualizar, concluir, detalhes).
   ===================================================== */

(function () {
    'use strict';

    /* ---------- Dados (iguais à imagem) ---------- */
    var TAMANHO_PAGINA = 8;

    var paineis = [];
    for (var i = 1; i <= 8; i++) {
        paineis.push({
            id: i,
            serie: 'PNL-' + String(i).padStart(4, '0'),
            marca: 'Sunova',
            modelo: 'Canadian solar 600W',
            potencia: 550,
            nf: '',
            data: '2026-09-11',
            status: i >= 7 ? 'Inativo' : 'Ativo'
        });
    }

    var manutencoes = [
        { status: 'Aberta', tipo: 'Preventiva', codigo: 'CS-2024-00765', os: 'OS84524', criada: '19/07/2026',
          descricao: 'A placa apresentou queda de desempenho.', previsao: '2026-10-09', usuario: 'Ana Souza' },
        { status: 'Em andamento', tipo: 'Preventiva', codigo: 'CS-2024-00765', os: 'OS84524', criada: '19/07/2026',
          descricao: 'A placa apresentou queda de desempenho.', previsao: '2026-10-09', usuario: 'Ana Souza' }
    ];

    var filtroStatus = '';
    var termoBusca = '';
    var pagina = 1;


    /* ---------- Utilidades ---------- */
    function escapar(texto) {
        var div = document.createElement('div');
        div.textContent = texto;
        return div.innerHTML;
    }

    function classeStatus(status) {
        return status === 'Ativo' ? 'badge-ativo' : 'badge-inativo';
    }

    function dataBR(iso) {
        var m = /^(\d{4})-(\d{2})-(\d{2})$/.exec(iso || '');
        return m ? m[3] + '/' + m[2] + '/' + m[1] : '—';
    }

    function hojeBR() {
        var d = new Date();
        return String(d.getDate()).padStart(2, '0') + '/' +
               String(d.getMonth() + 1).padStart(2, '0') + '/' + d.getFullYear();
    }

    function numeroBR(valor, casas) {
        return Number(valor).toFixed(casas).replace('.', ',');
    }


    /* =====================================================
       PAINÉIS
       ===================================================== */
    var tbody = document.getElementById('tbodyPaineis');
    var caixaPaginacao = document.getElementById('paginacao');
    var campoBusca = document.getElementById('buscaPainel');

    var btnFiltro = document.getElementById('btnFiltroStatus');
    var menuFiltro = document.getElementById('menuFiltroStatus');
    var rotuloFiltro = document.getElementById('rotuloFiltro');

    var dlgAddPainel = document.getElementById('dlg-add-painel');
    var formAddPainel = dlgAddPainel.querySelector('form');
    var dlgEditarPainel = document.getElementById('dlg-editar-painel');
    var formEditarPainel = dlgEditarPainel.querySelector('form');
    var painelEditando = null;

    function paineisFiltrados() {
        var termo = termoBusca.trim().toLowerCase();

        return paineis.filter(function (p) {
            if (filtroStatus && p.status !== filtroStatus) return false;
            if (termo && (p.modelo + ' ' + p.marca).toLowerCase().indexOf(termo) === -1) return false;
            return true;
        });
    }

    function renderMetricas() {
        var ativos = paineis.filter(function (p) { return p.status === 'Ativo'; }).length;
        var watts = paineis.reduce(function (soma, p) { return soma + Number(p.potencia); }, 0);

        document.getElementById('mTotal').textContent = paineis.length;
        document.getElementById('mPotencia').textContent = numeroBR(watts / 1000, 1) + ' kWp';
        document.getElementById('mAtivos').textContent = ativos + '/' + paineis.length;
    }

    function renderPaginacao(totalPaginas) {
        var html = '<button type="button" data-pag="anterior" aria-label="Anterior"' + (pagina === 1 ? ' disabled' : '') + '>‹</button>';

        for (var n = 1; n <= totalPaginas; n++) {
            html += '<button type="button" data-pag="' + n + '"' +
                    (n === pagina ? ' class="atual" aria-current="page"' : '') + '>' + n + '</button>';
        }

        html += '<button type="button" data-pag="proxima" aria-label="Próxima"' + (pagina === totalPaginas ? ' disabled' : '') + '>›</button>';
        caixaPaginacao.innerHTML = html;
    }

    function renderPaineis() {
        var lista = paineisFiltrados();
        var totalPaginas = Math.max(1, Math.ceil(lista.length / TAMANHO_PAGINA));
        if (pagina > totalPaginas) pagina = totalPaginas;

        var inicio = (pagina - 1) * TAMANHO_PAGINA;
        var visiveis = lista.slice(inicio, inicio + TAMANHO_PAGINA);

        if (visiveis.length === 0) {
            tbody.innerHTML = '<tr class="sem-resultado"><td colspan="7">Nenhum painel encontrado.</td></tr>';
        } else {
            tbody.innerHTML = visiveis.map(function (p) {
                return '' +
                    '<tr>' +
                        '<td>' + p.id + '</td>' +
                        '<td>' + escapar(p.marca) + '</td>' +
                        '<td>' + escapar(p.modelo) + '</td>' +
                        '<td>' + Number(p.potencia).toFixed(2) + '</td>' +
                        '<td>' + dataBR(p.data) + '</td>' +
                        '<td><span class="badge ' + classeStatus(p.status) + '">' + p.status + '</span></td>' +
                        '<td><button type="button" class="mais" data-editar="' + p.id + '" aria-label="Ações do painel ' + p.id + '">•••</button></td>' +
                    '</tr>';
            }).join('');
        }

        renderPaginacao(totalPaginas);
        renderMetricas();
    }


    /* ---------- Busca ---------- */
    campoBusca.addEventListener('input', function () {
        termoBusca = campoBusca.value;
        pagina = 1;
        renderPaineis();
    });


    /* ---------- Filtro de status ---------- */
    function abrirMenu(abrir) {
        menuFiltro.hidden = !abrir;
        btnFiltro.setAttribute('aria-expanded', abrir ? 'true' : 'false');
    }

    btnFiltro.addEventListener('click', function (e) {
        e.stopPropagation();
        abrirMenu(menuFiltro.hidden);
    });

    menuFiltro.addEventListener('click', function (e) {
        var item = e.target.closest('li[data-filtro]');
        if (!item) return;

        filtroStatus = item.getAttribute('data-filtro');
        rotuloFiltro.textContent = item.textContent;

        menuFiltro.querySelectorAll('li').forEach(function (li) {
            li.setAttribute('aria-selected', li === item ? 'true' : 'false');
        });

        pagina = 1;
        abrirMenu(false);
        renderPaineis();
    });

    document.addEventListener('click', function () { abrirMenu(false); });
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') abrirMenu(false);
    });


    /* ---------- Paginação ---------- */
    caixaPaginacao.addEventListener('click', function (e) {
        var botao = e.target.closest('button[data-pag]');
        if (!botao || botao.disabled) return;

        var alvo = botao.getAttribute('data-pag');
        if (alvo === 'anterior') pagina--;
        else if (alvo === 'proxima') pagina++;
        else pagina = Number(alvo);

        renderPaineis();
    });


    /* ---------- Adicionar painel ---------- */
    formAddPainel.addEventListener('submit', function (e) {
        if (e.defaultPrevented) return;                        // faltou campo
        if (!e.submitter || e.submitter.value !== 'ok') return;

        var f = formAddPainel.elements;
        var proximoId = paineis.reduce(function (maior, p) { return Math.max(maior, p.id); }, 0) + 1;

        paineis.push({
            id: proximoId,
            serie: f.serie.value.trim(),
            marca: f.marca.value.trim(),
            modelo: f.modelo.value.trim(),
            potencia: f.potencia.value,
            nf: f.nf.value.trim(),
            data: f.data.value,
            status: f.status.value
        });

        // vai para a página do painel novo, se ele aparecer no filtro atual
        var lista = paineisFiltrados();
        var posicao = lista.findIndex(function (p) { return p.id === proximoId; });
        if (posicao !== -1) pagina = Math.floor(posicao / TAMANHO_PAGINA) + 1;

        renderPaineis();
    });


    /* ---------- Editar painel ---------- */
    tbody.addEventListener('click', function (e) {
        var botao = e.target.closest('button[data-editar]');
        if (!botao) return;

        var id = Number(botao.getAttribute('data-editar'));
        painelEditando = paineis.filter(function (p) { return p.id === id; })[0];
        if (!painelEditando) return;

        var f = formEditarPainel.elements;
        f.serie.value = painelEditando.serie;
        f.marca.value = painelEditando.marca;
        f.modelo.value = painelEditando.modelo;
        f.potencia.value = painelEditando.potencia;
        f.nf.value = painelEditando.nf;
        f.data.value = painelEditando.data;
        f.status.value = painelEditando.status;

        dlgEditarPainel.showModal();
    });

    formEditarPainel.addEventListener('submit', function (e) {
        if (e.defaultPrevented) return;
        if (!e.submitter || e.submitter.value !== 'ok' || !painelEditando) return;

        var f = formEditarPainel.elements;

        painelEditando.serie    = f.serie.value.trim();
        painelEditando.marca    = f.marca.value.trim();
        painelEditando.modelo   = f.modelo.value.trim();
        painelEditando.potencia = f.potencia.value;
        painelEditando.nf       = f.nf.value.trim();
        painelEditando.data     = f.data.value;
        painelEditando.status   = f.status.value;

        renderPaineis();
    });


    /* =====================================================
       MANUTENÇÕES
       ===================================================== */
    var listaManut = document.getElementById('listaManut');

    var dlgCadManut = document.getElementById('dlg-cadastrar-manut');
    var formCadManut = dlgCadManut.querySelector('form');
    var dlgAtualizar = document.getElementById('dlg-atualizar-manut');
    var formAtualizar = dlgAtualizar.querySelector('form');
    var dlgDetalhes = document.getElementById('dlg-detalhes-manut');

    var manutAtual = null;     // manutenção aberta em "Atualizar"

    function classeStatusManut(status) {
        if (status === 'Aberta') return 'badge-aberta';
        if (status === 'Em andamento') return 'badge-andamento';
        return 'badge-concluida';
    }

    function classeTipo(tipo) {
        return { Preventiva: 'badge-preventiva', Corretiva: 'badge-corretiva', Preditiva: 'badge-preditiva' }[tipo] || 'badge-preventiva';
    }

    function renderManutencoes() {
        listaManut.innerHTML = manutencoes.map(function (m, idx) {
            var concluida = m.status === 'Concluída';

            return '' +
                '<article class="card-manut">' +
                    '<div class="linha"><span>Placa</span><span class="selos">' +
                        '<span class="badge ' + classeStatusManut(m.status) + '">' + escapar(m.status) + '</span>' +
                        '<span class="badge ' + classeTipo(m.tipo) + '">' + escapar(m.tipo) + '</span>' +
                    '</span></div>' +
                    '<p class="codigo">' + escapar(m.codigo) + '</p>' +
                    '<p class="id">ID: ' + escapar(m.os) + '</p>' +
                    '<div class="rodape">' +
                        '<span>' + escapar(m.criada) + '</span>' +
                        '<div class="botoes">' +
                            (concluida ? '' : '<button type="button" class="btn btn-atualizar" data-atualizar="' + idx + '">Atualizar</button>') +
                            '<button type="button" class="btn btn-detalhes" data-detalhes="' + idx + '">Ver detalhes</button>' +
                        '</div>' +
                    '</div>' +
                '</article>';
        }).join('');
    }

    function nomeUsuario() {
        var el = document.querySelector('.usuario span:not(.seta)');
        return el ? el.textContent.trim() : '—';
    }

    function proximoNumero(prefixo, digitos) {
        // número de exemplo; o definitivo virá do back-end
        var maior = manutencoes.reduce(function (max, m) {
            var n = parseInt(String(m[prefixo === 'OS' ? 'os' : 'codigo']).replace(/\D/g, '').slice(-digitos), 10) || 0;
            return Math.max(max, n);
        }, 0);
        return String(maior + 1).padStart(digitos, '0');
    }

    /* ---------- Cadastrar manutenção ---------- */
    formCadManut.addEventListener('submit', function (e) {
        if (e.defaultPrevented) return;                        // faltou campo
        if (!e.submitter || e.submitter.value !== 'ok') return;

        var f = formCadManut.elements;

        manutencoes.unshift({
            status: 'Aberta',
            tipo: f.tipo.value,
            codigo: 'CS-' + new Date().getFullYear() + '-' + proximoNumero('CS', 5),
            os: 'OS' + proximoNumero('OS', 5),
            criada: hojeBR(),
            descricao: f.descricao.value.trim(),
            previsao: f.previsao.value,
            usuario: nomeUsuario()
        });

        renderManutencoes();
    });

    /* ---------- Abrir "Atualizar" e "Ver detalhes" ---------- */
    listaManut.addEventListener('click', function (e) {
        var btnAtualizar = e.target.closest('button[data-atualizar]');
        var btnDetalhes = e.target.closest('button[data-detalhes]');

        if (btnAtualizar) {
            manutAtual = manutencoes[Number(btnAtualizar.getAttribute('data-atualizar'))];

            formAtualizar.elements.tipo.value = manutAtual.tipo;
            formAtualizar.elements.descricao.value = manutAtual.descricao;
            formAtualizar.elements.previsao.value = manutAtual.previsao;

            dlgAtualizar.showModal();
        }

        if (btnDetalhes) {
            var m = manutencoes[Number(btnDetalhes.getAttribute('data-detalhes'))];

            document.getElementById('det-tipo').value = m.tipo;
            document.getElementById('det-descricao').value = m.descricao;
            document.getElementById('det-previsao').value = dataBR(m.previsao);
            document.getElementById('det-usuario').value = m.usuario;

            dlgDetalhes.showModal();
        }
    });

    /* ---------- Salvar / Concluir ---------- */
    formAtualizar.addEventListener('submit', function (e) {
        if (e.defaultPrevented) return;
        if (!e.submitter || !manutAtual) return;

        var acao = e.submitter.value;
        if (acao !== 'salvar' && acao !== 'concluir') return;

        var f = formAtualizar.elements;

        manutAtual.tipo = f.tipo.value;
        manutAtual.descricao = f.descricao.value.trim();
        manutAtual.previsao = f.previsao.value;

        if (acao === 'concluir') manutAtual.status = 'Concluída';

        renderManutencoes();
    });


    /* ---------- Início ---------- */
    renderPaineis();
    renderManutencoes();
})();