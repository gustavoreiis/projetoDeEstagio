let pessoas = [];
let pessoasFiltradas = [];
let paginaAtual = 1;
const tamanhoPagina = 10;

const campoNome = document.getElementById("pesquisarNome");
const campoCpf = document.getElementById("pesquisarCpf");
const filtroGrupo = document.getElementById("filtroGrupo");

document.addEventListener("DOMContentLoaded", async function () {
    try {
        const response = await fetch("http://localhost:8080/pessoas");
        pessoas = await response.json();
        pessoasFiltradas = [...pessoas];
        mostrarPagina(paginaAtual);
    } catch (error) {
        console.error("Erro ao carregar pessoas:", error);
    }
});

function mostrarPagina(pagina) {
    const inicio = (pagina - 1) * tamanhoPagina;
    const fim = inicio + tamanhoPagina;
    const paginaPessoas = pessoasFiltradas.slice(inicio, fim);

    preencherTabelaParticipantes(paginaPessoas);
    atualizarPaginacao(pagina);
}

function preencherTabelaParticipantes(participantes) {
    const tabela = document.getElementById("tabelaPessoas");
    tabela.innerHTML = "";

    if (participantes.length === 0) {
        document.getElementById("lista-vazio").classList.remove("d-none");
        return;
    }
    document.getElementById("lista-vazio").classList.add("d-none");

    participantes.forEach(participante => {
        const frequenciaExibicao = participante.frequencia == -1 ? "-" : participante.frequencia + "%";

        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td class="text-center">${participante.nome}</td>
            <td class="text-center">${formatarCpf(participante.cpf)}</td>
            <td class="text-center">${formatarTelefone(participante.telefone)}</td>
            <td class="text-center">${participante.grupo}</td>
            <td class="text-center">${formatarData(participante.dataNascimento)}</td>
            <td class="text-center">${frequenciaExibicao}</td>
            <td class="text-center">
                <button class="btn vermelho-border" data-bs-toggle="modal" data-bs-target="#modalDetalhes">
                    <i class="bi bi-eye"></i>
                </button>
                <button class="btn vermelho-border" data-bs-toggle="modal" data-bs-target="#modalHistorico">
                    <i class="bi bi-calendar-event"></i>
                </button>
            </td>
        `;

        const btnDetalhes = tr.querySelector('button[data-bs-target="#modalDetalhes"]');
        btnDetalhes.addEventListener("click", () => carregarDetalhesParticipante(participante));

        const btnAtividades = tr.querySelector('button[data-bs-target="#modalHistorico"]');
        btnAtividades.addEventListener("click", () => carregarHistoricoParticipante(participante));

        tabela.appendChild(tr);
    });
}

campoNome.addEventListener("input", aplicarFiltros);
campoCpf.addEventListener("input", aplicarFiltros);
filtroGrupo.addEventListener("change", aplicarFiltros);

function aplicarFiltros() {
    const termoNome = campoNome.value.trim().toLowerCase();
    const termoCpf = campoCpf.value.replace(/\D/g, "");
    const filtro = filtroGrupo.value;

    pessoasFiltradas = pessoas.filter(p => {
        const nomeMatch = p.nome.toLowerCase().includes(termoNome);
        const cpfMatch = p.cpf.includes(termoCpf);

        if (!filtro) return nomeMatch && cpfMatch;
        if (filtro === "Participante") {
            return nomeMatch && cpfMatch && p.grupo === "Participante";
        }
        if (filtro === "Servo") {
            return nomeMatch && cpfMatch && (p.grupo === "Servo" || p.grupo !== "Participante");
        }
        return nomeMatch && cpfMatch && p.grupo === filtro;
    });

    paginaAtual = 1;
    mostrarPagina(paginaAtual);
}

function atualizarPaginacao(pagina) {
    const totalPaginas = Math.ceil(pessoasFiltradas.length / tamanhoPagina);
    const paginacao = document.getElementById("paginacao");
    paginacao.innerHTML = "";

    if (totalPaginas <= 1) return;

    const ul = document.createElement("ul");
    ul.classList.add("pagination");

    const liAnterior = document.createElement("li");
    liAnterior.classList.add("page-item");
    if (pagina === 1) liAnterior.classList.add("disabled");
    liAnterior.innerHTML = `<a class="page-link" href="#">Anterior</a>`;
    liAnterior.addEventListener("click", () => {
        if (paginaAtual > 1) {
            paginaAtual--;
            mostrarPagina(paginaAtual);
        }
    });
    ul.appendChild(liAnterior);

    for (let i = 1; i <= totalPaginas; i++) {
        const li = document.createElement("li");
        li.classList.add("page-item");
        if (i === pagina) li.classList.add("active");
        li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        li.addEventListener("click", () => {
            paginaAtual = i;
            mostrarPagina(paginaAtual);
        });
        ul.appendChild(li);
    }

    const liProximo = document.createElement("li");
    liProximo.classList.add("page-item");
    if (pagina === totalPaginas) liProximo.classList.add("disabled");
    liProximo.innerHTML = `<a class="page-link" href="#">Próxima</a>`;
    liProximo.addEventListener("click", () => {
        if (paginaAtual < totalPaginas) {
            paginaAtual++;
            mostrarPagina(paginaAtual);
        }
    });
    ul.appendChild(liProximo);

    paginacao.appendChild(ul);
}


function carregarDetalhesParticipante(participante) {
    console.log("Abrir detalhes de:", participante.nome);
}

async function carregarHistoricoParticipante(participante) {
    const response = await fetch(`http://localhost:8080/pessoas/historico/${participante.idPessoa}`);
    const historico = await response.json();

    const tabelaHistoricoAtividades = document.getElementById("historicoAtividades");
    const tabelaHistoricoEncontros = document.getElementById("historicoEncontros");

    tabelaHistoricoAtividades.innerHTML = "";
    tabelaHistoricoEncontros.innerHTML = "";

    if (historico.atividades.length === 0) {
        tabelaHistoricoAtividades.innerHTML = `
            <tr>
                <td colspan="3" class="text-center text-muted">Nenhuma atividade registrada</td>
            </tr>
        `;
    } else {
        historico.atividades
            .sort((a, b) => new Date(b.data) - new Date(a.data))
            .forEach(atividade => {
                const iconePresenca = atividade.presente
                    ? `<i class="bi bi-check-circle text-success"></i>`
                    : `<i class="bi bi-x-circle text-danger"></i>`;

                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${atividade.descricao}</td>
                    <td>${formatarData(atividade.data)}</td>
                    <td class="text-center">${iconePresenca}</td>
                `;
                tabelaHistoricoAtividades.appendChild(tr);
            });
    }

    if (historico.encontros.length === 0) {
        tabelaHistoricoEncontros.innerHTML = `
            <tr>
                <td colspan="3" class="text-center text-muted">Nenhum encontro registrado</td>
            </tr>
        `;
    } else {
        historico.encontros
            .sort((a, b) => new Date(b.data) - new Date(a.data))
            .forEach(encontro => {
                const tr = document.createElement("tr");
                encontro.data = formatarDataEncontro(encontro.data);
                tr.innerHTML = `
                    <td>${encontro.titulo}</td>
                    <td>${formatarData(encontro.data)}</td>
                `;
                tabelaHistoricoEncontros.appendChild(tr);
            });
    }
}


















document.getElementById('pesquisarCpf').addEventListener('input', function (e) {
    e.target.value = formatarCpf(e.target.value);
});

function formatarTelefone(telefone) {
    if (!telefone) return "-";
    telefone = telefone.replace(/\D/g, '');
    if (telefone.length === 11) return `(${telefone.slice(0, 2)}) ${telefone.slice(2, 7)}-${telefone.slice(7)}`;
    if (telefone.length === 10) return `(${telefone.slice(0, 2)}) ${telefone.slice(2, 6)}-${telefone.slice(6)}`;
    return telefone;
}

function formatarData(data) {
    if (!data) return "-";
    const partes = data.split('-');
    if (partes.length !== 3) return data;
    return `${partes[2]}/${partes[1]}/${partes[0]}`;
}

function formatarCpf(valor) {
  valor = valor.replace(/\D/g, '');
  valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
  valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
  valor = valor.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
  return valor;
}

function formatarDataEncontro(dataHora) {
    if (!dataHora) return "-";
    const data = new Date(dataHora);
    return data.toLocaleDateString("pt-BR");
}
