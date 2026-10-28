/* ===== pop-ups.js ===== */
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

/* ===== empresa.js ===== */
/* =====================================================
   APOLLO — EMPRESA
   Usa o pop-ups.js para abrir/fechar/validar os <dialog>.
   Aqui: lista de filiais, menu ⋮ (editar/excluir),
   cadastrar/editar filial e editar empresa.
   ===================================================== */

(function () {
    'use strict';

    /* ---------- Dados (iguais à imagem) ---------- */
    var filiais = [
        { nome: 'Matriz',        logradouro: 'Av. das nações, 123 - centro',    cidade: 'São Paulo - SP 00000-000' },
        { nome: 'Filial centro', logradouro: 'Rua da conceição, 456 - centro',  cidade: 'São Paulo - SP 00000-000' }
    ];

    var lista        = document.getElementById('listaFiliais');
    var btnAdicionar = document.getElementById('btnAdicionarFilial');

    var dlgFilial  = document.getElementById('dlg-filial');
    var formFilial = dlgFilial.querySelector('form');
    var tituloDlg  = document.getElementById('dlg-filial-titulo');
    var btnSalvarF = document.getElementById('dlg-filial-ok');

    var dlgExcluir   = document.getElementById('dlg-excluir-filial');
    var nomeExcluir  = document.getElementById('excluir-filial-nome');

    var dlgEmpresa  = document.getElementById('dlg-editar-empresa');
    var formEmpresa = dlgEmpresa.querySelector('form');

    var editando = -1;   // -1 = cadastrando
    var excluindo = -1;


    /* ---------- Utilidades ---------- */
    function escapar(texto) {
        var div = document.createElement('div');
        div.textContent = texto;
        return div.innerHTML;
    }

    function fixarPadroes(form) {
        form.querySelectorAll('input').forEach(function (i) {
            i.defaultValue = i.value;
        });
    }


    /* ---------- Render das filiais ---------- */
    var ICONE_PIN  = '<svg viewBox="0 0 24 24"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></svg>';
    var ICONE_OLHO = '<svg viewBox="0 0 24 24"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>';

    function renderFiliais() {
        lista.innerHTML = filiais.map(function (f, i) {
            return '' +
                '<article class="card-filial">' +
                    '<div class="filial-info">' +
                        '<div class="icone-filial">' + ICONE_PIN + '</div>' +
                        '<div>' +
                            '<span class="filial-nome">' + escapar(f.nome) + '</span>' +
                            '<span class="filial-endereco">' + escapar(f.logradouro) + '<br>' + escapar(f.cidade) + '</span>' +
                        '</div>' +
                    '</div>' +
                    '<div class="filial-acoes">' +
                        '<button type="button" class="btn btn-ver" data-acao="ver">' + ICONE_OLHO + 'Ver detalhes</button>' +
                        '<button type="button" class="btn-kebab" data-acao="menu" data-i="' + i + '" aria-haspopup="true" aria-expanded="false" aria-label="Mais ações de ' + escapar(f.nome) + '">&#8942;</button>' +
                        '<div class="menu-filial" role="menu" hidden>' +
                            '<button type="button" role="menuitem" data-acao="editar" data-i="' + i + '">Editar filial</button>' +
                            '<button type="button" role="menuitem" class="perigo" data-acao="excluir" data-i="' + i + '">Excluir filial</button>' +
                        '</div>' +
                    '</div>' +
                '</article>';
        }).join('');
    }


    /* ---------- Menu ⋮ ---------- */
    function fecharMenus() {
        lista.querySelectorAll('.menu-filial').forEach(function (m) { m.hidden = true; });
        lista.querySelectorAll('.btn-kebab').forEach(function (b) { b.setAttribute('aria-expanded', 'false'); });
    }

    document.addEventListener('click', function (e) {
        if (!e.target.closest('.filial-acoes')) fecharMenus();
    });

    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') fecharMenus();
    });

    lista.addEventListener('click', function (e) {
        var botao = e.target.closest('[data-acao]');
        if (!botao) return;

        var acao = botao.getAttribute('data-acao');
        var i = Number(botao.getAttribute('data-i'));

        if (acao === 'menu') {
            var menu = botao.parentNode.querySelector('.menu-filial');
            var abrir = menu.hidden;
            fecharMenus();
            menu.hidden = !abrir;
            botao.setAttribute('aria-expanded', String(abrir));
            return;
        }

        if (acao === 'editar') {
            fecharMenus();
            abrirFilial(i);
        } else if (acao === 'excluir') {
            fecharMenus();
            excluindo = i;
            nomeExcluir.textContent = filiais[i].nome;
            dlgExcluir.showModal();
        }
        if (acao === 'ver') {
            window.location.href = 'detalhes-filial.html';   // abre a tela de detalhes da filial
        }
    });


    /* ---------- Cadastrar / editar filial ---------- */
    function abrirFilial(indice) {
        editando = indice;

        var f = formFilial.elements;

        if (indice >= 0) {
            tituloDlg.textContent = 'Editar filial';
            btnSalvarF.textContent = 'Salvar';
            f.nome.value = filiais[indice].nome;
            f.logradouro.value = filiais[indice].logradouro;
            f.cidade.value = filiais[indice].cidade;
        } else {
            tituloDlg.textContent = 'Cadastrar filial';
            btnSalvarF.textContent = 'Cadastrar';
        }

        dlgFilial.showModal();
    }

    btnAdicionar.addEventListener('click', function () {
        abrirFilial(-1);
    });

    formFilial.addEventListener('submit', function (e) {
        if (e.defaultPrevented) return;                       // faltou campo
        if (!e.submitter || e.submitter.value !== 'ok') return;

        var f = formFilial.elements;
        var dados = {
            nome:       f.nome.value.trim(),
            logradouro: f.logradouro.value.trim(),
            cidade:     f.cidade.value.trim()
        };

        if (editando >= 0) {
            filiais[editando] = dados;
        } else {
            filiais.push(dados);
        }

        renderFiliais();
    });


    /* ---------- Excluir filial ---------- */
    dlgExcluir.addEventListener('close', function () {
        if (dlgExcluir.returnValue === 'ok' && excluindo >= 0) {
            filiais.splice(excluindo, 1);
            renderFiliais();
        }
        excluindo = -1;
    });


    /* ---------- Editar empresa ---------- */
    formEmpresa.addEventListener('submit', function (e) {
        if (e.defaultPrevented) return;
        if (!e.submitter || e.submitter.value !== 'ok') return;

        var f = formEmpresa.elements;

        function texto(chave, valor) {
            document.querySelectorAll('[data-emp="' + chave + '"]').forEach(function (el) {
                el.textContent = valor;
            });
        }

        texto('nome', f.nome.value.trim());
        texto('email', f.email.value.trim());
        texto('telefone', f.telefone.value.trim());
        texto('logradouro', f.logradouro.value.trim());
        texto('cidade', f.cidade.value.trim());

        var sigla = f.nome.value.trim().split(' ')[0];
        texto('sigla', sigla);

        // o reset feito ao fechar não pode voltar aos valores antigos
        fixarPadroes(formEmpresa);
    });


    /* ---------- Links do menu ainda sem tela ---------- */
    document.addEventListener('click', function (e) {
        var vazio = e.target.closest('a[href="#"]');
        if (vazio) e.preventDefault();
    });


    /* ---------- Início ---------- */
    renderFiliais();
})();