// Abre o pop-up cujo id está em data-abrir="..." e fecha ao clicar fora.
document.addEventListener('click', (e) => {
  const gatilho = e.target.closest('[data-abrir]');
  if (gatilho) { document.getElementById(gatilho.dataset.abrir)?.showModal(); return; }
  if (e.target.matches('dialog.pop')) e.target.close(); // clique no fundo
});
// Para abrir por código: abrirPopup('dlg-cadastrar-string')
window.abrirPopup = (id) => document.getElementById(id)?.showModal();

// Trocar foto de perfil (câmera)
const fotoInput = document.getElementById('foto-input');
document.getElementById('foto-btn').addEventListener('click', () => fotoInput.click());
fotoInput.addEventListener('change', () => {
  const arq = fotoInput.files[0];
  if (!arq) return;
  const url = URL.createObjectURL(arq);
  document.querySelectorAll('[data-foto]').forEach((el) => {
    el.style.backgroundImage = `url(${url})`;
    el.textContent = '';
  });
});

// Mostra o tipo de usuário escolhido (Gerente / Técnico / Operador)
(function () {
  if (!window.Apollo) return;
  var nome = Apollo.nomePerfil();
  var selo = document.querySelector('.selo-cargo');
  if (selo) selo.textContent = nome;
  document.querySelectorAll('.linha').forEach(function (l) {
    var rotulo = l.querySelector('span');
    if (rotulo && rotulo.textContent.trim() === 'Tipo de usuário') l.querySelector('b').textContent = nome;
  });
})();