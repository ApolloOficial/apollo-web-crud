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

/* ################ Tela: Filial ################ */
/* =====================================================
   APOLLO — DETALHES DA FILIAL
   Usa o pop-ups.js (abrir/fechar/validação) como base.
   Este arquivo cuida de: cards de inversores, resumo ao
   vivo do pop-up "Cadastrar inversor" e inativar filial.
   ===================================================== */

(function () {
    'use strict';

    /* ---------- Dados (iguais à imagem) ---------- */
    var inversores = [
        { codigo: 'INV-OSA-01', marca: 'Sungrow', modelo: 'SG250HX', potencia: '250', serie: 'A2310001234', data: '10/03/2024', status: 'Ativo' },
        { codigo: 'INV-OSA-01', marca: 'Sungrow', modelo: 'SG250HX', potencia: '250', serie: 'A2310001234', data: '10/03/2024', status: 'Ativo' },
        { codigo: 'INV-OSA-01', marca: 'Sungrow', modelo: 'SG250HX', potencia: '250', serie: 'A2310001234', data: '10/03/2024', status: 'Ativo' },
        { codigo: 'INV-OSA-01', marca: 'Sungrow', modelo: 'SG250HX', potencia: '250', serie: 'A2310001234', data: '10/03/2024', status: 'Ativo' }
    ];

    var lista = document.getElementById('listaInversores');
    var dlgCadastrar = document.getElementById('dlg-cadastrar-inversor');
    var formCadastrar = dlgCadastrar.querySelector('form');


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
        // aceita "aaaa-mm-dd" (input date); se vazio, usa hoje
        var m = /^(\d{4})-(\d{2})-(\d{2})$/.exec(iso || '');
        if (m) return m[3] + '/' + m[2] + '/' + m[1];

        var d = new Date();
        return String(d.getDate()).padStart(2, '0') + '/' +
               String(d.getMonth() + 1).padStart(2, '0') + '/' +
               d.getFullYear();
    }

    function formatarPotencia(valor) {
        return String(valor).replace('.', ',');
    }


    /* ---------- Render dos cards ---------- */
    function renderInversores() {
        lista.innerHTML = inversores.map(function (inv) {
            var potencia = inv.potencia !== '' ? ' · ' + escapar(formatarPotencia(inv.potencia)) + ' kW' : '';

            return '' +
                '<article class="card-string">' +
                    '<div>' +
                        '<span class="tipo">Inversor</span>' +
                        '<span class="codigo">' + escapar(inv.codigo) + '</span>' +
                        '<span class="mppt">' + escapar(inv.marca + ' ' + inv.modelo) + potencia + '</span>' +
                        '<span class="paineis">Nº de série: ' + escapar(inv.serie) + '</span>' +
                        '<span class="data">Instalado em ' + escapar(inv.data) + '</span>' +
                    '</div>' +
                    '<div class="lado-direito">' +
                        '<span class="badge ' + classeStatus(inv.status) + '">' + escapar(inv.status) + '</span>' +
                        '<a href="detalhes-inversor.html" class="btn btn-detalhes">Ver detalhes</a>' +
                    '</div>' +
                '</article>';
        }).join('');
    }


    /* ---------- Resumo ao vivo (pop-up cadastrar inversor) ---------- */
    var camposResumo = {
        codigo:   function (f) { return f.elements.codigo.value.trim(); },
        marca:    function (f) { return f.elements.marca.value.trim(); },
        modelo:   function (f) { return f.elements.modelo.value.trim(); },
        potencia: function (f) { return f.elements.potencia.value !== '' ? formatarPotencia(f.elements.potencia.value) + ' kW' : ''; },
        status:   function (f) { return f.elements.status.value; },
        data:     function (f) { return f.elements.data.value ? formatarData(f.elements.data.value) : ''; }
    };

    function atualizarResumo() {
        Object.keys(camposResumo).forEach(function (chave) {
            var alvo = formCadastrar.querySelector('[data-resumo="' + chave + '"]');
            var texto = camposResumo[chave](formCadastrar);

            alvo.textContent = texto !== '' ? texto : '—';
            alvo.classList.toggle('preenchido', texto !== '');
        });
    }

    formCadastrar.addEventListener('input', atualizarResumo);
    formCadastrar.addEventListener('change', atualizarResumo);
    formCadastrar.addEventListener('reset', function () {
        setTimeout(atualizarResumo, 0);
    });


    /* ---------- Cadastrar inversor ---------- */
    formCadastrar.addEventListener('submit', function (e) {
        // se o pop-ups.js barrou por campo vazio, não cadastra
        if (e.defaultPrevented) return;
        if (!e.submitter || e.submitter.value !== 'ok') return;

        inversores.push({
            codigo:   formCadastrar.elements.codigo.value.trim(),
            marca:    formCadastrar.elements.marca.value.trim(),
            modelo:   formCadastrar.elements.modelo.value.trim(),
            potencia: formCadastrar.elements.potencia.value,
            serie:    formCadastrar.elements.serie.value.trim(),
            data:     formatarData(formCadastrar.elements.data.value),
            status:   formCadastrar.elements.status.value
        });

        renderInversores();
    });


    /* ---------- Inativar / Ativar filial ---------- */
    var dlgInativar = document.getElementById('dlg-inativar-filial');

    dlgInativar.addEventListener('close', function () {
        if (dlgInativar.returnValue !== 'ok') return;

        var botao = document.querySelector('[data-abrir="dlg-inativar-filial"]');
        var ativar = botao.textContent.trim() === 'Inativar filial';
        var texto = ativar ? 'Inativo' : 'Ativo';

        document.querySelectorAll('[data-status-filial]').forEach(function (b) {
            b.textContent = texto;
            b.classList.remove('badge-ativo', 'badge-inativo');
            b.classList.add(classeStatus(texto));
        });

        botao.textContent = ativar ? 'Ativar filial' : 'Inativar filial';
    });


    /* ---------- Início ---------- */
    renderInversores();
    atualizarResumo();
})();