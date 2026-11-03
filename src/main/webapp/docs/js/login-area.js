(function () {
  'use strict';

  var campoArea = document.getElementById('area');
  var botoesArea = Array.prototype.slice.call(document.querySelectorAll('.area[data-area]'));
  if (!campoArea || botoesArea.length === 0) return;

  function selecionarArea(area) {
    var existe = botoesArea.some(function (botao) {
      return botao.getAttribute('data-area') === area;
    });
    if (!existe) return;

    campoArea.value = area;
    botoesArea.forEach(function (botao) {
      botao.setAttribute('aria-pressed', String(botao.getAttribute('data-area') === area));
    });
  }

  botoesArea.forEach(function (botao) {
    botao.addEventListener('click', function () {
      selecionarArea(botao.getAttribute('data-area'));
    });
  });

  selecionarArea(campoArea.value || 'gerente');
})();
