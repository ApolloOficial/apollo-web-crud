/* =====================================================
   APOLLO — DETALHES DO INVERSOR
   ===================================================== */

(function () {
    'use strict';

    /* ---------- Dados das strings (iguais à imagem) ---------- */
    let strings = [
        { codigo: 'STR-OSA-01-01', mppt: 1, entrada: 1, paineis: 24, inativos: 1, data: '15/03/2024', status: 'Ativo' },
        { codigo: 'STR-OSA-01-02', mppt: 1, entrada: 2, paineis: 24, inativos: 0, data: '15/03/2024', status: 'Ativo' },
        { codigo: 'STR-OSA-01-01', mppt: 1, entrada: 1, paineis: 24, inativos: 1, data: '15/03/2024', status: 'Ativo' },
        { codigo: 'STR-OSA-01-02', mppt: 1, entrada: 2, paineis: 24, inativos: 0, data: '15/03/2024', status: 'Ativo' }
    ];

    let lista      = document.getElementById('listaStrings');
    let overlay    = document.getElementById('overlay');
    let form       = document.getElementById('formString');
    let btnAbrir   = document.getElementById('btnCadastrarString');
    let btnCancel  = document.getElementById('btnCancelar');

    let btnInativar   = document.getElementById('btnInativar');
    let btnExcluir    = document.getElementById('btnExcluir');
    let btnEditar     = document.getElementById('btnEditar');
    let badgeInversor = document.getElementById('badgeInversor');
    let badgeStatus   = document.getElementById('badgeStatus');


    /* ---------- Utilidades ---------- */
    function escapar(texto) {
        let div = document.createElement('div');
        div.textContent = texto;
        return div.innerHTML;
    }

    function hoje() {
        let d = new Date();
        let dia = String(d.getDate()).padStart(2, '0');
        let mes = String(d.getMonth() + 1).padStart(2, '0');
        return dia + '/' + mes + '/' + d.getFullYear();
    }

    function classeStatus(status) {
        return status === 'Ativo' ? 'badge-ativo' : 'badge-inativo';
    }


    /* ---------- Render das strings ---------- */
    function textoPaineis(s) {
        let txt = s.paineis + ' painéis';
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
                        '<button type="button" class="btn btn-detalhes">Ver detalhes</button>' +
                    '</div>' +
                '</article>';
        }).join('');
    }


    /* ---------- Pop-up ---------- */
    function limparErros() {
        form.querySelectorAll('.campo').forEach(function (c) {
            c.classList.remove('invalido');
        });
    }

    function abrirModal() {
        form.reset();
        limparErros();
        overlay.hidden = false;
        document.body.style.overflow = 'hidden';
        document.getElementById('codigo').focus();
    }

    function fecharModal() {
        overlay.hidden = true;
        document.body.style.overflow = '';
        btnAbrir.focus();
    }

    btnAbrir.addEventListener('click', abrirModal);
    btnCancel.addEventListener('click', fecharModal);

    overlay.addEventListener('mousedown', function (e) {
        if (e.target === overlay) fecharModal();
    });

    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape' && !overlay.hidden) fecharModal();
    });

    // limpa o erro assim que o usuário preenche o campo
    form.querySelectorAll('input, select').forEach(function (campo) {
        let evento = campo.tagName === 'SELECT' ? 'change' : 'input';
        campo.addEventListener(evento, function () {
            if (campo.value.trim() !== '') {
                campo.closest('.campo').classList.remove('invalido');
            }
        });
    });

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        let valido = true;
        let primeiroInvalido = null;

        form.querySelectorAll('.campo').forEach(function (campo) {
            let input = campo.querySelector('input, select');
            if (input.value.trim() === '') {
                campo.classList.add('invalido');
                valido = false;
                if (!primeiroInvalido) primeiroInvalido = input;
            } else {
                campo.classList.remove('invalido');
            }
        });

        if (!valido) {
            primeiroInvalido.focus();
            return;
        }

        strings.push({
            codigo:   form.codigo.value.trim(),
            mppt:     form.mppt.value,
            entrada:  form.entrada.value,
            paineis:  0,
            inativos: 0,
            data:     hoje(),
            status:   form.status.value
        });

        renderStrings();
        fecharModal();
    });


    /* ---------- Ações do inversor ---------- */
    let inversorAtivo = true;

    btnInativar.addEventListener('click', function () {
        inversorAtivo = !inversorAtivo;

        let texto = inversorAtivo ? 'Ativo' : 'Inativo';
        let classe = classeStatus(texto);

        [badgeInversor, badgeStatus].forEach(function (b) {
            b.textContent = texto;
            b.classList.remove('badge-ativo', 'badge-inativo');
            b.classList.add(classe);
        });

        btnInativar.textContent = inversorAtivo ? 'Inativar' : 'Ativar';
    });

    btnExcluir.addEventListener('click', function () {
        if (confirm('Deseja realmente excluir o inversor INV-OSA-01?')) {
            alert('Inversor excluído.');
        }
    });

    btnEditar.addEventListener('click', function () {
        alert('Abrir tela de edição do inversor.');
    });


    /* ---------- Início ---------- */
    renderStrings();
})();