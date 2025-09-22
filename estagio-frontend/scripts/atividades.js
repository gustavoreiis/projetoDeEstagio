async function abrirModalCriar () {
    const modalCriar = new bootstrap.Modal(document.getElementById('modalCriarAtividade'));
    modalCriar.show();
}

const collapse = document.getElementById('pesquisaCollapse');
const icone = document.getElementById('iconeCollapse');
collapse.addEventListener('shown.bs.collapse', () => {
    icone.classList.remove('bi-caret-down-fill');
    icone.classList.add('bi-caret-up-fill');
});
collapse.addEventListener('hidden.bs.collapse', () => {
    icone.classList.remove('bi-caret-up-fill');
    icone.classList.add('bi-caret-down-fill');
});

async function carregarAtividades() {
    
}