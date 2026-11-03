document.querySelectorAll('[data-mascara]').forEach(function (campo) {
    var tipo = campo.getAttribute('data-mascara');

    function aplicar() {
        var n = campo.value.replace(/\D/g, '');
        if (tipo === 'cnpj') {
            n = n.slice(0, 14)
                .replace(/^(\d{2})(\d)/, '$1.$2')
                .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
                .replace(/\.(\d{3})(\d)/, '.$1/$2')
                .replace(/(\d{4})(\d)/, '$1-$2');
        } else if (tipo === 'cpf') {
            n = n.slice(0, 11)
                .replace(/(\d{3})(\d)/, '$1.$2')
                .replace(/(\d{3})(\d)/, '$1.$2')
                .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
        } else if (tipo === 'cep') {
            n = n.slice(0, 8).replace(/(\d{5})(\d)/, '$1-$2');
        } else if (tipo === 'telefone') {
            n = n.slice(0, 11);
            n = n.length > 10
                ? n.replace(/^(\d{2})(\d{5})(\d{0,4})$/, '($1) $2-$3')
                : n.replace(/^(\d{2})(\d{0,4})(\d{0,4})$/, '($1) $2-$3');
            n = n.replace(/[-\s(]+$/, '');
        }
        campo.value = n;
    }

    campo.addEventListener('input', aplicar);
    aplicar();
});
