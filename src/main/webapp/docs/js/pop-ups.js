// Abre o pop-up cujo id está em data-abrir="..." e fecha ao clicar fora.
document.addEventListener('click', (e) => {
  const gatilho = e.target.closest('[data-abrir]');
  if (gatilho) { document.getElementById(gatilho.dataset.abrir)?.showModal(); return; }
  if (e.target.matches('dialog.pop')) e.target.close(); // clique no fundo
});
// Para abrir por código: abrirPopup('dlg-cadastrar-string')
window.abrirPopup = (id) => document.getElementById(id)?.showModal();