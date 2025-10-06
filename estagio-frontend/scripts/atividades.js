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

/*window.addEventListener('DOMContentLoaded', () => {
    const containerVazio = document.getElementById('lista-vazio');
    const containerGrupos = document.getElementById('lista-grupos');
})*/

document.getElementById('formFiltros').addEventListener('input', () => {
    //const atividades = fetch('http://localhost:8080/atividades');

    
    const atividades = [
        {
            descricao: "Ensaio de música",
            data: "2025-10-05",
            grupo: "musica",
            status: "ativo"
        },
        {
            descricao: "Reunião de intercessão",
            data: "2025-10-10",
            grupo: "intercessao",
            status: "concluido"
        }
        // Adicione mais atividades conforme necessário
    ];


    const texto = document.getElementById('pesquisarInput').ariaValueMax.toLowerCase();
    const dataInicio = document.getElementById('dataInicio').value;
    const dataFim = document.getElementById('dataFim').value;
    const grupo = document.getElementById('grupoFiltro').value;
    const status = document.querySelector('input[name="status"]:checked')?.value;

    const filtradas = atividades.filter(atividade => {
        const descricaoFiltro = atividade.descricao.toLowerCase().includes(texto);
        const grupoFiltro = !grupo || atividade.grupo === grupo;
        const statusFiltro = !status || atividade.status === status;
        const dataFiltro = 
            (!dataInicio || atividade.data >= dataInicio) &&
            (!dataFim || atividade.data <= dataFim);

            return descricaoFiltro && grupoFiltro && statusFiltro && dataFiltro;
    });
    carregarAtividades(filtradas);
})

function carregarAtividades(lista) {
    const containerVazio = document.getElementById('lista-vazio');
    const containerAtividades = document.getElementById('lista-atividades');
    containerGrupos.innerHTML = '';

    if (lista.length === 0) {
        containerVazio.classList.remove('d-none');
        return;
    }

    lista.forEach(atividade => {
        const card = document.createElement('div');
        card.className = 'col-md-4';
        card.innerHTML = `
        <div class="card-100">
            <div class="card-header d-flex justify-content-between align-items-center>
                <h6 class="card-title mb-0>${atividade.descricao}</h6>
                <div class="d-flex gap-2">
                    <i class="bi bi-pencil-square fs-6 text-black" style="cursor: pointer;" onclick='abrirModalEditarAtividade()'></i>
                    <i class="bi bi-trash fs-6 text-black" style="cursor: pointer;" onclick="modalExcluirAtividade()"></i>
                </div>
            </div>
            <div class="card-body">
                <p class="card-text"><strong>Data: </strong>${atividade.data}</p>
                <p class="card-text"><strong>Grupo: </strong>${atividade.grupo}</p>
                <p class="card-text"><strong>Status: </strong>${atividade.status}</p>
            </div>
            <div class="card-footer text-center d-flex justify-content-around">
                <a class="btn vermelho vermelho-hover btn-sm border" onclick="abrirModalVisualizarAtividade()">Registrar Presenças</a>
            </div>
        </div>
        `;
        containerAtividades
    })
}