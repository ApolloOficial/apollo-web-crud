// "Voltar" volta para a página anterior; se não houver, vai para o Dashboard
document.getElementById('voltar').addEventListener('click', function (e) {
    if (document.referrer && history.length > 1) {
        e.preventDefault();
        history.back();
    }
});