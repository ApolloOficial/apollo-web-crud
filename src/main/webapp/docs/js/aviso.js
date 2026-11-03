(function () {
    var aviso = document.getElementById('aviso');
    var confirmacao = document.getElementById('confirmacao');
    var formPendente = null;

    function limparUrl() {
        if (location.search.indexOf('ok=') !== -1 && window.history && history.replaceState) {
            var url = new URL(location.href);
            url.searchParams.delete('ok');
            history.replaceState(null, '', url.pathname + url.search);
        }
    }

    if (aviso) {
        aviso.addEventListener('close', limparUrl);
        aviso.showModal();
    }

    document.querySelectorAll('dialog.aviso').forEach(function (dialogo) {
        dialogo.addEventListener('click', function (evento) {
            if (evento.target === dialogo) {
                dialogo.close();
            }
        });
        dialogo.querySelectorAll('[data-fechar]').forEach(function (botao) {
            botao.addEventListener('click', function () {
                dialogo.close();
            });
        });
    });

    document.addEventListener('submit', function (evento) {
        var formulario = evento.target;
        var mensagem = formulario.getAttribute ? formulario.getAttribute('data-confirmar') : null;
        if (!mensagem || !confirmacao) {
            return;
        }
        evento.preventDefault();
        formPendente = formulario;
        confirmacao.querySelector('[data-texto]').textContent = mensagem;
        confirmacao.showModal();
    });

    if (confirmacao) {
        confirmacao.querySelector('[data-confirmar-envio]').addEventListener('click', function () {
            confirmacao.close();
            if (formPendente) {
                var formulario = formPendente;
                formPendente = null;
                formulario.removeAttribute('data-confirmar');
                formulario.submit();
            }
        });
    }
})();
