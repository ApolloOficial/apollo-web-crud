// Abre o pop-up cujo id está em data-abrir="..." e fecha ao clicar fora.
document.addEventListener('click', (e) => {
  const gatilho = e.target.closest('[data-abrir]');
  if (gatilho) { document.getElementById(gatilho.dataset.abrir)?.showModal(); return; }
  if (e.target.matches('dialog.pop')) e.target.close(); // clique no fundo
});
// Para abrir por código: abrirPopup('dlg-cadastrar-string')
window.abrirPopup = (id) => document.getElementById(id)?.showModal();

// Modal "Editar inversor": só liga os eventos nas telas que têm esse modal
// (antes dava erro em telas sem ele, como o Dashboard).
const btnEditar = document.getElementById("btnEditarInversor");
const popupEditar = document.getElementById("editarInversorModal");

if (btnEditar && popupEditar) {
  const btnFechar = popupEditar.querySelector(".modal-close");
  const btnCancelar = popupEditar.querySelector(".button-cancel");

  const abrir = () => {
    popupEditar.style.display = "flex";
    popupEditar.setAttribute("aria-hidden", "false");
  };
  const fechar = () => {
    popupEditar.style.display = "none";
    popupEditar.setAttribute("aria-hidden", "true");
  };

  btnEditar.addEventListener("click", abrir);
  btnFechar?.addEventListener("click", fechar);
  btnCancelar?.addEventListener("click", fechar);
}