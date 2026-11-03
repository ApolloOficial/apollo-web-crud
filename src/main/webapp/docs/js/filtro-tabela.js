(function () {
    var campoBusca = document.getElementById('busca');
    var campoStatus = document.getElementById('filtro-status');
    var linhas = document.querySelectorAll('[data-linha]');
    var aviso = document.getElementById('sem-resultado');

    function soNumeros(valor) {
        return valor.replace(/\D/g, '');
    }

    function filtrar() {
        var texto = campoBusca ? campoBusca.value.trim().toLowerCase() : '';
        var numeros = soNumeros(texto);
        var situacao = campoStatus ? campoStatus.value : '';
        var visiveis = 0;

        linhas.forEach(function (linha) {
            var conteudo = linha.textContent.toLowerCase();
            var combinaTexto = conteudo.indexOf(texto) !== -1
                || (numeros !== '' && soNumeros(conteudo).indexOf(numeros) !== -1);
            var combinaStatus = situacao === '' || linha.getAttribute('data-status') === situacao;
            var mostrar = combinaTexto && combinaStatus;
            linha.hidden = !mostrar;
            if (mostrar) {
                visiveis++;
            }
        });

        if (aviso) {
            aviso.hidden = visiveis !== 0 || linhas.length === 0;
        }
    }

    if (campoBusca) {
        campoBusca.addEventListener('input', filtrar);
    }
    if (campoStatus) {
        campoStatus.addEventListener('change', filtrar);
    }
})();
