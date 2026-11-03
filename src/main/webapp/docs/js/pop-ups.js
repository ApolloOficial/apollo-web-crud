document.addEventListener('click', (e) => {
  const gatilho = e.target.closest('[data-abrir]');
  if (gatilho) { document.getElementById(gatilho.dataset.abrir)?.showModal(); return; }
  if (e.target.matches('dialog.pop')) e.target.close();
});
window.abrirPopup = (id) => document.getElementById(id)?.showModal();
