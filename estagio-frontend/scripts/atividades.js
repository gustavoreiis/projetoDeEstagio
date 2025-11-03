verificarAutenticacao();

function abrirModalCriar() {
    document.getElementById('modalGrupoLabel').textContent = "Cadastrar Nova Atividade";
    document.getElementById('btnSalvarAtividade').textContent = "Cadastrar";

    document.getElementById('idAtividade').value = "";
    document.getElementById('descricao').value = "";
    document.getElementById('data').value = "";
    document.getElementById('grupo').value = "";

    const modalCriar = new bootstrap.Modal(document.getElementById('modalCriarAtividade'));
    modalCriar.show();
}

function abrirModalEditar(idAtividade) {
    const atividade = atividades.find(a => a.id === idAtividade);
    document.getElementById('modalGrupoLabel').textContent = "Editar Atividade";
    document.getElementById('btnSalvarAtividade').textContent = "Salvar";
    document.getElementById('idAtividade').value = atividade.id;
    document.getElementById('descricao').value = atividade.descricao;
    document.getElementById('data').value = atividade.dataAtividade;
    document.getElementById('grupo').value = atividade.grupoDePessoas;

    const modalCriar = new bootstrap.Modal(document.getElementById('modalCriarAtividade'));
    modalCriar.show();
}

function abrirModalExcluir(idAtividade) {
    document.getElementById('idAtividade').value = idAtividade;
    const modalExcluir = new bootstrap.Modal(document.getElementById('modalExcluirAtividade'));
    modalExcluir.show();
}

document.getElementById('confirmarExcluir').addEventListener('click', async () => {
    const id = document.getElementById('idAtividade').value;
    try {
        const response = await fetch(`http://localhost:8080/atividades/${id}`, {
            method: 'DELETE'
        });
        if (response.ok) {
            const modalExcluir = document.getElementById('modalExcluirAtividade');
            const modalInstance = bootstrap.Modal.getInstance(modalExcluir) || new bootstrap.Modal(modalExcluir);
            modalInstance.hide();
            await buscarAtividades();
        }
    } catch (error) {
        console.error(error);
    }
})

document.getElementById('formCadastroAtividade').addEventListener('submit', async (event) => {
    event.preventDefault();
    const id = document.getElementById('idAtividade').value
    const descricao = document.getElementById('descricao').value;
    const dataAtividade = document.getElementById('data').value;
    const grupoDePessoas = document.getElementById('grupo').value;

    const atividade = {
        descricao,
        dataAtividade,
        grupoDePessoas
    };

    const url = id ? `http://localhost:8080/atividades/${id}` : 'http://localhost:8080/atividades';
    const method = id ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(atividade)
        })

        if (response.ok) {
            const modalCriar = document.getElementById('modalCriarAtividade');
            const modalInstance = bootstrap.Modal.getInstance(modalCriar) || new bootstrap.Modal(modalCriar);
            modalInstance.hide();
            await buscarAtividades();
            event.target.removeEventListener;
        }
    } catch (error) {
        console.error(error);
    }
})

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
    containerVazio.classList.add('d-none');

    lista.forEach(atividade => {
        const card = document.createElement('div');
        card.classList.add("col-md-4");

        const corStatus = atividade.ativa ? "bg-success" : "bg-secondary";
        const textoStatus = atividade.ativa ? "Ativo" : "Concluído";

        card.innerHTML = `
        <div class="card h-100">
          <div class="card-header d-flex justify-content-between align-items-center">
            <h6 class="card-title mb-0">${atividade.descricao}</h6>
            <div class="d-flex gap-2">
            <i class="bi bi-pencil-square fs-6 text-black" style="cursor: pointer;" onclick='abrirModalEditar(${atividade.id})'></i>
            <i class="bi bi-trash fs-6 text-black" style="cursor: pointer;" onclick="abrirModalExcluir(${atividade.id})"></i>
            </div>
          </div>
          <div class="card-body">
            <p class="card-text"><strong>Data: </strong> ${atividade.dataAtividade}</p>
            <p class="card-text"><strong>Grupo: </strong>${atividade.grupoDePessoas}</p>
            <p class="card-text"><strong>Status: </strong><span class="badge ${corStatus} text-white">${textoStatus}</span></p>
          </div>
          <div class="card-footer text-center d-flex justify-content-around">
            <a class="btn vermelho vermelho-hover text-white btn-sm border" onclick="abrirModalPresenca(${atividade.id})">Registrar Presenças</a>
          </div>
        </div>
      `;
        containerAtividades.appendChild(card);
    });
}

function aplicarFiltros() {
    const texto = document.getElementById('pesquisarInput').value.toLowerCase();
    const dataInicio = document.getElementById('dataInicio').value;
    const dataFim = document.getElementById('dataFim').value;
    const grupo = document.getElementById('grupoFiltro').value;
    const status = document.querySelector('input[name="status"]:checked')?.value;

    const filtradas = atividades.filter(atividade => {
        let match = true;

        if (texto && !atividade.descricao.toLowerCase().includes(texto)) match = false;
        if (grupo && atividade.grupoDePessoas !== grupo) match = false;
        if (status) {
            const statusBool = status === "true";
            if (atividade.ativa !== statusBool) match = false;
        }
        if (dataInicio && new Date(atividade.dataAtividade) < new Date(dataInicio)) match = false;
        if (dataFim && new Date(atividade.dataAtividade) > new Date(dataFim)) match = false;

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

async function abrirModalPresenca(idAtividade) {
    const atividade = atividades.find(a => a.id === idAtividade);
    const lista = document.getElementById('listaPessoas');
    lista.innerHTML = '';

    const response = await fetch(`http://localhost:8080/atividades/${idAtividade}`);
    const pessoas = await response.json();

    if (!pessoas || pessoas.length === 0) {
        document.getElementById('lista-vazia').classList.remove('d-none');
        document.getElementById('btnSalvarPresenca').disabled = true;
    }
    
    pessoas.forEach(pessoa => {
        const item = document.createElement('div');
        item.classList.add('border', 'rounded', 'py-2', 'px-4',  'row', 'align-items-center', 'mb-2');

        item.innerHTML = `
            <div class="form-check col-md-11">
                <input class="form-check-input" type="checkbox" value="${pessoa.idPessoa}" id="pessoa${pessoa.idPessoa}" ${pessoa.presente ? 'checked' : ''}>
                <label class="form-check-label mx-3" for="pessoa${pessoa.idPessoa}">
                    ${pessoa.nome}
                </label>
            </div>
            <button type="button" class="btn col-md-1" id="btnObs${pessoa.idPessoa}" title="Adicionar observação">
                <i class="bi bi-chat-left-text"></i>
            </button>
            <div class="mt-2 col-md-12 ${pessoa.observacao ? '' : 'd-none'}" id="obs-container-${pessoa.idPessoa}">
                <input type="text" class="form-control" id="obs${pessoa.idPessoa}" placeholder="Observação" value="${pessoa.observacao ?? ''}">
            </div>
        `;
        lista.appendChild(item);

        const btnObs = document.getElementById(`btnObs${pessoa.idPessoa}`);
        const obsContainer = document.getElementById(`obs-container-${pessoa.idPessoa}`);

        btnObs.addEventListener('click', () => {
            obsContainer.classList.toggle('d-none');
        });
    })

    document.getElementById('modalPresencaLabel').textContent = `Registrar Presenças - ${atividade.descricao}`;
    document.getElementById('atividadeData').innerHTML = `<strong>Data:</strong> ${atividade.dataAtividade}   `;
    document.getElementById('atividadeGrupo').innerHTML = `<strong>Grupo:</strong> ${atividade.grupoDePessoas}`;
    document.getElementById('idAtividadePresenca').value = idAtividade;
    const modalPresenca = new bootstrap.Modal(document.getElementById('modalPresenca'));
    modalPresenca.show();
}

async function salvarPresencas(idAtividade) {
    const lista = document.getElementById('listaPessoas');
    const presencas = [];

    lista.querySelectorAll('div.row').forEach(item => {
        const checkbox = item.querySelector('input[type="checkbox"]');
        const observacao = item.querySelector('input[type="text"]');

        presencas.push({
            idPessoa: parseInt(checkbox.value),
            idAtividade: idAtividade,
            presente: checkbox.checked,
            observacao: observacao.value || ""
        });
    });

    try {
        const response = await fetch('http://localhost:8080/presencas', {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(presencas)
        })
    } catch (error) {
        console.error(error);
    }
};

document.getElementById('btnSalvarPresenca').addEventListener('click', async () => {
    const idAtividade = document.getElementById('idAtividadePresenca').value;
    await salvarPresencas(idAtividade);
    buscarAtividades();
    const modalPresenca = document.getElementById('modalPresenca');
    const modalInstance = bootstrap.Modal.getInstance(modalPresenca) || new bootstrap.Modal(modalPresenca);
    modalInstance.hide();
});