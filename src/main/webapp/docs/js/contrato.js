(function () {
    'use strict';

    /* Tela somente leitura: nada aqui é digitado pelo usuário.
       Os dados chegam logo após o login, de uma destas formas:
         1) no HTML:  <form id="form-contrato" data-contrato='{"empresa":"...","contratante":"...","valor":1500,"inicio":"2026-01-15","status":"Ativo"}'>
         2) por código: preencherContrato({ empresa, contratante, valor, inicio, status })
       valor aceita número (1500) ou texto ("1500,50"); inicio aceita "AAAA-MM-DD" ou texto já formatado. */

    var CAMPOS = ['empresa', 'contratante', 'valor', 'inicio', 'status'];
    var form = document.getElementById('form-contrato');
    var f = form.elements;

    function moeda(v) {
        if (v === null || v === undefined || v === '') return '';
        if (typeof v === 'string' && v.indexOf('R$') > -1) return v;
        var n = typeof v === 'number' ? v : parseFloat(String(v).replace(/\./g, '').replace(',', '.'));
        return isNaN(n) ? String(v) : n.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    }

    function dataBR(v) {
        var m = /^(\d{4})-(\d{2})-(\d{2})/.exec(v || '');
        return m ? m[3] + '/' + m[2] + '/' + m[1] : (v || '');
    }

    function texto(v) {
        return v === null || v === undefined ? '' : String(v).trim();
    }

    function preencher(dados) {
        dados = dados || {};
        var valores = {
            empresa: texto(dados.empresa),
            contratante: texto(dados.contratante),
            valor: moeda(dados.valor),
            inicio: dataBR(dados.inicio),
            status: texto(dados.status)
        };

        CAMPOS.forEach(function (k) {
            f[k].value = valores[k];
            var dd = form.querySelector('[data-resumo="' + k + '"]');
            dd.textContent = valores[k];
            dd.classList.toggle('preenchido', valores[k] !== '');
        });
    }

    window.preencherContrato = preencher;
    form.addEventListener('submit', function (e) { e.preventDefault(); });

    // dados entregues no próprio HTML (ex.: gerados pelo servidor após o login)
    var inicial = form.getAttribute('data-contrato');
    if (inicial) {
        try { preencher(JSON.parse(inicial)); } catch (e) {}
    }
})();