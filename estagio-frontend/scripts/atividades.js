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

async function buscarAtividades() {
    try {
        const response = await fetch("http://localhost:8080/atividades");
        atividades = await response.json();
        exibirAtividades(atividades);
    } catch (error) {
        console.error(error);
        exibirAtividades([]);
    }
}

function exibirAtividades(lista) {
    const containerVazio = document.getElementById('lista-vazio');
    const containerAtividades = document.getElementById('lista-atividades');

    containerAtividades.innerHTML = "";

    if (!lista || lista.length === 0) {
        containerVazio.classList.remove("d-none");
        return;
    }

    lista.forEach(atividade => {
        const card = document.createElement('div');
        card.classList.add("col-md-4");

        card.innerHTML = `
        <div class="card h-100">
          <div class="card-header d-flex justify-content-between align-items-center">
            <h6 class="card-title mb-0">${atividade.descricao}</h6>
            <div class="d-flex gap-2">
            <i class="bi bi-pencil-square fs-6 text-black" style="cursor: pointer;" onclick='abrirModalEditar()'></i>
            <i class="bi bi-trash fs-6 text-black" style="cursor: pointer;" onclick="abrirModalExcluir()"></i>
            </div>
          </div>
          <div class="card-body">
            <p class="card-text"><strong>Data: </strong> ${atividade.data}</p>
            <p class="card-text"><strong>Grupo: </strong>${atividade.grupo}</p>
            <p class="card-text"><strong>Status: </strong>${atividade.status}</p>
          </div>
          <div class="card-footer text-center d-flex justify-content-around">
            <a class="btn vermelho vermelho-hover text-white btn-sm border" onclick="">Registrar Presenças</a>
          </div>
        </div>
      `;
      containerAtividades.appendChild(card);
    });
}

function aplicarFiltros() {
    const texto = document.getElementById('pesquisarInput').ariaValueMax.toLowerCase();
    const dataInicio = document.getElementById('dataInicio').value;
    const dataFim = document.getElementById('dataFim').value;
    const grupo = document.getElementById('grupoFiltro').value;
    const status = document.querySelector('input[name="status"]:checked')?.value;

    const filtradas = atividades.filter(atividade => {
        let match = true;

        if (texto && !atividade.descricao.toLowerCase().includes(texto)) match = false;
        if (grupo && atividade.grupo !== grupo) match = false;
        if (status && atividade.status !== status) match = false;
        if (dataInicio && new Date(atividade.data) < new Date(dataInicio)) match = false;
        if (dataFim && new Date(atividade.data) > new Date(dataFim)) match = false;

        return match;
    });
    exibirAtividades(filtradas);
}

document.addEventListener("DOMContentLoaded", () => {
    buscarAtividades();
    const form = document.getElementById("formFiltros");
    formFiltros.addEventListener("input", aplicarFiltros);
    formFiltros.addEventListener("input", aplicarFiltros);
});