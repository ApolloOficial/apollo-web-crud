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

/* ################ Tela: Inversor ################ */
/* =====================================================
   APOLLO — TELA DO INVERSOR (versão unificada)
   Usa o pop-ups.js para abrir/fechar/validar os <dialog>.
   Aqui: lista de strings, cadastrar string, editar e
   inativar inversor.
   ===================================================== */

(function () {
    'use strict';

    /* ---------- Dados (iguais à imagem) ---------- */
    var strings = [
        { codigo: 'STR-OSA-01-01', mppt: 1, entrada: 1, paineis: 24, inativos: 1, data: '15/03/2024', status: 'Ativo' },
        { codigo: 'STR-OSA-01-02', mppt: 1, entrada: 2, paineis: 24, inativos: 0, data: '15/03/2024', status: 'Ativo' },
        { codigo: 'STR-OSA-01-01', mppt: 1, entrada: 1, paineis: 24, inativos: 1, data: '15/03/2024', status: 'Ativo' },
        { codigo: 'STR-OSA-01-02', mppt: 1, entrada: 2, paineis: 24, inativos: 0, data: '15/03/2024', status: 'Ativo' }
    ];

    var lista = document.getElementById('listaStrings');

    var dlgCadastrar = document.getElementById('dlg-cadastrar-string');
    var formCadastrar = dlgCadastrar.querySelector('form');

    var dlgEditar = document.getElementById('dlg-editar-inversor');
    var formEditar = dlgEditar.querySelector('form');

    var dlgInativar = document.getElementById('dlg-inativar-inversor');
    var botaoInativar = document.querySelector('[data-abrir="dlg-inativar-inversor"]');


    /* ---------- Utilidades ---------- */
    function escapar(texto) {
        var div = document.createElement('div');
        div.textContent = texto;
        return div.innerHTML;
    }

    function classeStatus(status) {
        return status === 'Ativo' ? 'badge-ativo' : 'badge-inativo';
    }

    function formatarData(iso) {
        var m = /^(\d{4})-(\d{2})-(\d{2})$/.exec(iso || '');
        if (m) return m[3] + '/' + m[2] + '/' + m[1];

        var d = new Date();
        return String(d.getDate()).padStart(2, '0') + '/' +
               String(d.getMonth() + 1).padStart(2, '0') + '/' +
               d.getFullYear();
    }

    // guarda os valores atuais como "padrão" do formulário,
    // para o reset (feito ao fechar o pop-up) não voltar ao antigo
    function fixarPadroes(form) {
        form.querySelectorAll('input').forEach(function (i) {
            i.defaultValue = i.value;
        });
        form.querySelectorAll('select').forEach(function (s) {
            Array.prototype.forEach.call(s.options, function (o, idx) {
                o.defaultSelected = (idx === s.selectedIndex);
            });
        });
    }


    /* ---------- Strings ---------- */
    function textoPaineis(s) {
        var txt = s.paineis + ' painéis';
        if (s.inativos > 0) {
            txt += ' (' + s.inativos + (s.inativos === 1 ? ' inativo)' : ' inativos)');
        }
        return txt;
    }

    function renderStrings() {
        lista.innerHTML = strings.map(function (s) {
            return '' +
                '<article class="card-string">' +
                    '<div>' +
                        '<span class="tipo">String</span>' +
                        '<span class="codigo">' + escapar(s.codigo) + '</span>' +
                        '<span class="mppt">MPPT ' + escapar(String(s.mppt)) + ' · Entrada ' + escapar(String(s.entrada)) + '</span>' +
                        '<span class="paineis">' + textoPaineis(s) + '</span>' +
                        '<span class="data">Instalada em ' + escapar(s.data) + '</span>' +
                    '</div>' +
                    '<div class="lado-direito">' +
                        '<span class="badge ' + classeStatus(s.status) + '">' + escapar(s.status) + '</span>' +
                        '<a href="detalhes-string.html" class="btn btn-detalhes">Ver detalhes</a>' +
                    '</div>' +
                '</article>';
        }).join('');
    }

    formCadastrar.addEventListener('submit', function (e) {
        if (e.defaultPrevented) return;                       // faltou campo
        if (!e.submitter || e.submitter.value !== 'ok') return;

        strings.push({
            codigo:   formCadastrar.elements.codigo.value.trim(),
            mppt:     formCadastrar.elements.mppt.value,
            entrada:  formCadastrar.elements.entrada.value,
            paineis:  0,
            inativos: 0,
            data:     formatarData(''),
            status:   formCadastrar.elements.status.value
        });

        renderStrings();
    });


    /* ---------- Status do inversor ---------- */
    function statusAtual() {
        return document.querySelector('[data-status-inversor]').textContent.trim();
    }

    function definirStatus(texto) {
        document.querySelectorAll('[data-status-inversor]').forEach(function (b) {
            b.textContent = texto;
            b.classList.remove('badge-ativo', 'badge-inativo');
            b.classList.add(classeStatus(texto));
        });

        botaoInativar.textContent = texto === 'Ativo' ? 'Inativar' : 'Ativar';

        formEditar.elements.status.value = texto;
        fixarPadroes(formEditar);
    }

    dlgInativar.addEventListener('close', function () {
        if (dlgInativar.returnValue !== 'ok') return;
        definirStatus(statusAtual() === 'Ativo' ? 'Inativo' : 'Ativo');
    });


    /* ---------- Editar inversor ---------- */
    function texto(chave, valor) {
        document.querySelectorAll('[data-inv="' + chave + '"]').forEach(function (el) {
            el.textContent = valor;
        });
    }

    formEditar.addEventListener('submit', function (e) {
        if (e.defaultPrevented) return;
        if (!e.submitter || e.submitter.value !== 'ok') return;

        var f = formEditar;
        var potencia = f.elements.potencia.value !== '' ? Number(f.elements.potencia.value) : null;

        texto('codigo', f.elements.codigo.value.trim());
        texto('serie', f.elements.serie.value.trim());
        texto('marca', f.elements.marca.value.trim());
        texto('modelo', f.elements.modelo.value.trim());
        texto('potencia', potencia !== null ? potencia.toFixed(2).replace('.', ',') : '—');
        texto('data', f.elements.data.value ? formatarData(f.elements.data.value) : '—');
        texto('subtitulo',
            f.elements.marca.value.trim() + ' ' + f.elements.modelo.value.trim() +
            (potencia !== null ? ' · ' + String(potencia).replace('.', ',') + ' kW' : ''));

        definirStatus(f.elements.status.value);
        fixarPadroes(f);
    });


    /* ---------- Início ---------- */
    renderStrings();
})();