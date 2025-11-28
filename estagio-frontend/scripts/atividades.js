verificarAutenticacao();

let paginaAtual = 0;
const tamanhoPagina = 6;
let atividades = [];

document.addEventListener('DOMContentLoaded', () => {
    const hoje = new Date();
    const primeiroDia = new Date(hoje.getFullYear(), hoje.getMonth(), 1);
    const ultimoDia = new Date(hoje.getFullYear(), hoje.getMonth() + 1, 0);

    document.getElementById("dataInicio").value = primeiroDia.toISOString().split("T")[0];
    document.getElementById("dataFim").value = ultimoDia.toISOString().split("T")[0];
    const statusAtivo = document.querySelector('input[name="status"][value="true"]');
    if (statusAtivo) statusAtivo.checked = true;
    carregarAtividades(0);
});

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
            await carregarAtividades(0);
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
            await carregarAtividades(0)
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

async function carregarAtividades(pagina = 0) {
    paginaAtual = pagina;

    const nome = document.getElementById('pesquisarInput').value.trim();
    const dataInicio = document.getElementById('dataInicio').value;
    const dataFim = document.getElementById('dataFim').value;
    const grupo = document.getElementById('grupoFiltro').value;
    const statusSelecionado = document.querySelector('input[name="status"]:checked')?.value;
    let status = null;
    if (statusSelecionado === "true") status = true;
    if (statusSelecionado === "false") status = false;

    const params = new URLSearchParams();
    params.append('page', paginaAtual);
    params.append('size', tamanhoPagina);

    if (nome) params.append('nome', nome);
    if (dataInicio) params.append('dataInicio', dataInicio);
    if (dataFim) params.append('dataFim', dataFim);
    if (grupo) params.append('grupo', grupo);
    if (status !== null) params.append("status", status);

    try {
        const url = `http://localhost:8080/atividades?${params.toString()}`;
        const response = await fetch(url);

        if (!response.ok) throw new Error('Erro ao carregar atividades');

        const data = await response.json();
        atividades = data.content;
        exibirAtividadesPagina(data);
    } catch (error) {
        console.error(error);
        exibirAtividadesPagina({ content: [], totalPages: 0, number: 0, first: true, last: true });
    }
}

function exibirAtividadesPagina(pageData) {
    const lista = pageData.content || [];
    const containerVazio = document.getElementById('lista-vazio');
    const containerAtividades = document.getElementById('lista-atividades');

    containerAtividades.innerHTML = "";

    if (!lista || lista.length === 0) {
        containerVazio.classList.remove("d-none");
        document.getElementById('paginacao').classList.add('d-none');
        return;
    }

    containerVazio.classList.add('d-none');
    document.getElementById('paginacao').classList.remove('d-none');

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
            <p class="card-text"><strong>Data: </strong> ${formatarData(atividade.dataAtividade)}</p>
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

    atualizarPaginacao(pageData);
}

function atualizarPaginacao(pageData) {
    const paginacao = document.getElementById("paginacao");
    paginacao.innerHTML = "";

    const ul = document.createElement("ul");
    ul.classList.add("pagination");

    const liAnterior = document.createElement("li");
    liAnterior.classList.add("page-item");
    if (pageData.first) liAnterior.classList.add("disabled");
    liAnterior.innerHTML = `<a class="page-link" href="#">Anterior</a>`;
    liAnterior.addEventListener("click", (e) => {
        e.preventDefault();
        if (!pageData.first) {
            paginaAtual--;
            carregarAtividades(paginaAtual);
        }
    });
    ul.appendChild(liAnterior);

    for (let i = 0; i < pageData.totalPages; i++) {
        const li = document.createElement("li");
        li.classList.add("page-item");
        if (i === pageData.number) li.classList.add("active");
        li.innerHTML = `<a class="page-link" href="#">${i + 1}</a>`;
        li.addEventListener("click", (e) => {
            e.preventDefault();
            paginaAtual = i;
            carregarAtividades(paginaAtual);
        });
        ul.appendChild(li);
    }

    const liProximo = document.createElement("li");
    liProximo.classList.add("page-item");
    if (pageData.last) liProximo.classList.add("disabled");
    liProximo.innerHTML = `<a class="page-link" href="#">Próxima</a>`;
    liProximo.addEventListener("click", (e) => {
        e.preventDefault();
        if (!pageData.last) {
            paginaAtual++;
            carregarAtividades(paginaAtual);
        }
    });
    ul.appendChild(liProximo);

    paginacao.appendChild(ul);
}

document.getElementById('btnPesquisar').addEventListener('click', () => carregarAtividades(0));

async function abrirModalPresenca(idAtividade) {
    const atividade = atividades.find(a => a.id === idAtividade);
    const lista = document.getElementById('listaPessoas');
    document.getElementById('lista-vazia').classList.add('d-none');
    lista.innerHTML = '';

    const response = await fetch(`http://localhost:8080/atividades/${idAtividade}`);
    const pessoas = await response.json();

    if (!pessoas || pessoas.length === 0) {
        document.getElementById('lista-vazia').classList.remove('d-none');
        document.getElementById('btnSalvarPresenca').disabled = true;
    }

    pessoas.forEach(pessoa => {
        const item = document.createElement('div');
        item.classList.add('border', 'rounded', 'py-2', 'px-4', 'row', 'align-items-center', 'mb-2');

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
    await carregarAtividades(0)
    const modalPresenca = document.getElementById('modalPresenca');
    const modalInstance = bootstrap.Modal.getInstance(modalPresenca) || new bootstrap.Modal(modalPresenca);
    modalInstance.hide();
});

function formatarData(data) {
    const partesData = data.split('-');

    const ano = partesData[0];
    const mes = partesData[1];
    const dia = partesData[2];

    return `${dia}/${mes}/${ano}`;
}