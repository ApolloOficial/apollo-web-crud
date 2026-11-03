var formulario = document.getElementById('form-usuario');
var senha = document.getElementById('senha');
var confirmacao = document.getElementById('confirmar-senha');
var aviso = document.getElementById('aviso-senha');

formulario.addEventListener('submit', function (evento) {
    if (senha.value !== confirmacao.value) {
        evento.preventDefault();
        aviso.textContent = 'As senhas devem ser iguais.';
        aviso.hidden = false;
        confirmacao.focus();
    }
});

confirmacao.addEventListener('input', function () {
    aviso.hidden = true;
});
