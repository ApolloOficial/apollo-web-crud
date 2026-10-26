(function () {
  'use strict';

  var FALLBACK = {
    "notificacao.png": "https://www.figma.com/api/mcp/asset/0b5c141c-ce48-4306-8418-41fd54dc6ae2.png",
    "notificacao-ponto.svg": "https://www.figma.com/api/mcp/asset/a24b21b1-b46c-440a-8b17-ca070b344ed3.svg",
    "menu-down.svg": "https://www.figma.com/api/mcp/asset/b36db3f2-15f5-4e85-9084-a39dde4b73ed.svg",
    "avatar.png": "https://www.figma.com/api/mcp/asset/08850a49-495c-46d5-a6f7-27f4b38b013d.png",
    "icon-membros.svg": "https://www.figma.com/api/mcp/asset/9aca3830-fd50-41b6-8ab9-35ec2004db10.svg",
    "icon-lotes.svg": "https://www.figma.com/api/mcp/asset/9b8e6f98-f71a-4aae-9561-ec2a8246c6bc.svg",
    "icon-potencia.svg": "https://www.figma.com/api/mcp/asset/c5666d72-ab90-4d3a-b6bd-c19804c1b9bb.svg",
    "icon-paineis.svg": "https://www.figma.com/api/mcp/asset/5a047625-af0b-4983-8d30-574e0c2d2376.svg",
    "icon-metas-alcancadas.svg": "https://www.figma.com/api/mcp/asset/deab69d4-8627-4da4-bbff-dc981047ad8e.svg",
    "icon-metas-pendentes.svg": "https://www.figma.com/api/mcp/asset/2ea4c36c-807c-491f-b020-c7959184471f.svg",
    "icon-folha.svg": "https://www.figma.com/api/mcp/asset/08ef79b9-4cda-47aa-98bb-a2e4ffc688e8.svg",
    "grafico.svg": "https://www.figma.com/api/mcp/asset/a3c4f9d7-2cf1-4165-86e6-3de73420f9e7.svg",
    "logo-apollo.svg": "https://www.figma.com/api/mcp/asset/83f66daa-c704-43c3-a76f-8ae3dda19e12.svg",
    "nav-configuracoes.svg": "https://www.figma.com/api/mcp/asset/b4cce857-9c65-4a37-a2c4-be5fbcfc9e7f.svg",
    "nav-usuarios.svg": "https://www.figma.com/api/mcp/asset/d2c1f9ab-7d00-41dd-8102-f62b57051694.svg",
    "nav-dashboard.svg": "https://www.figma.com/api/mcp/asset/b2f512a2-2ef0-4672-958e-f2fdf06d3148.svg",
    "nav-lotes.svg": "https://www.figma.com/api/mcp/asset/91822d49-9623-4f6b-8302-dddfb3d106fb.svg",
    "nav-filial.svg": "https://www.figma.com/api/mcp/asset/e22eaa6a-1c95-499a-a8fb-fcb14b711e11.svg",
    "nav-manutencoes.svg": "https://www.figma.com/api/mcp/asset/10e7295f-9d7b-4a2c-9dd7-ffaf9a7a52e0.svg",
    "nav-contrato.svg": "https://www.figma.com/api/mcp/asset/a3bf2287-192a-4087-8619-65c63f8493e8.svg",
    "nav-empresa.svg": "https://www.figma.com/api/mcp/asset/0d52b5ad-4a6c-4752-8a8d-49f24afd86da.svg"
};

  document.querySelectorAll('img[data-asset]').forEach(function (img) {
    var nome = img.getAttribute('data-asset');
    img.addEventListener('error', function onErr() {
      img.removeEventListener('error', onErr);
      if (FALLBACK[nome]) img.src = FALLBACK[nome];
    });
    img.src = 'assets/' + nome;
  });

  var meses = document.getElementById('meses');
  for (var i = 1; i <= 12; i++) {
    var s = document.createElement('span');
    s.textContent = (i < 10 ? '0' : '') + i + '/1';
    s.style.left = (43 * (i - 1)) + 'px';
    meses.appendChild(s);
  }

  var btn = document.getElementById('btnMenu');
  var menu = document.getElementById('menuUsuario');
  function fechar() { menu.hidden = true; btn.setAttribute('aria-expanded', 'false'); }
  btn.addEventListener('click', function (e) {
    e.stopPropagation();
    var abrir = menu.hidden;
    menu.hidden = !abrir;
    btn.setAttribute('aria-expanded', String(abrir));
  });
  document.addEventListener('click', fechar);
  document.addEventListener('keydown', function (e) { if (e.key === 'Escape') fechar(); });

  var tela = document.getElementById('tela');
  function ajustar() {
    var escala = Math.min(window.innerWidth / 1440, window.innerHeight / 732);
    var x = (window.innerWidth - 1440 * escala) / 2;
    var y = (window.innerHeight - 732 * escala) / 2;
    tela.style.transform = 'translate(' + x + 'px,' + y + 'px) scale(' + escala + ')';
  }
  window.addEventListener('resize', ajustar);
  ajustar();
})();
