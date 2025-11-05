verificarAutenticacao();

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
                <button class="btn vermelho-border" data-bs-toggle="modal" data-bs-target="#modalParticipante">
                    <i class="bi bi-eye"></i>
                </button>
                <button class="btn vermelho-border" data-bs-toggle="modal" data-bs-target="#modalHistorico">
                    <i class="bi bi-calendar-event"></i>
                </button>
            </td>
        `;

        const btnDetalhes = tr.querySelector('button[data-bs-target="#modalParticipante"]');
        btnDetalhes.addEventListener("click", () => abrirModalParticipante(participante));

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

async function carregarSolicitacoesAprovacao() {
    try {
        const response = await fetch("http://localhost:8080/pessoas/solicitacoes");
        const solicitacoes = await response.json();

        preencherTabela("tabelaPendentes", solicitacoes.pendentes, "PENDENTE");
        preencherTabela("tabelaAprovados", solicitacoes.aprovados, "COORDENADOR");
        preencherTabela("tabelaNegados", solicitacoes.negados, "NEGADO");
    }
    catch (error) {
    }
}

document.getElementById('opcaoAprovacoes').addEventListener('click', async function () {

});

function preencherTabela(idTabela, lista, statusPadrao) {
    const tabela = document.getElementById(idTabela);
    tabela.innerHTML = "";

    if (!lista || lista.length === 0) {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td colspan="2" class="text-center text-muted py-3">
                Nenhum elemento encontrado.
            </td>
        `;
        tabela.appendChild(tr);
        return;
    }

    lista.forEach(pessoa => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td class="text-center">${pessoa.nome}</td>
            <td class="text-center">
                <select class="form-select form-select-sm status-select" data-id="${pessoa.idPessoa}">
                    <option value="PENDENTE" ${pessoa.statusCoordenador === "PENDENTE" ? "selected" : ""}>Pendente</option>
                    <option value="COORDENADOR" ${pessoa.statusCoordenador === "COORDENADOR" ? "selected" : ""}>Aprovado</option>
                    <option value="NEGADO" ${pessoa.statusCoordenador === "NEGADO" ? "selected" : ""}>Negado</option>
                </select>
            </td>
        `;
        tabela.appendChild(tr);
    });
}

document.getElementById('salvarSolicitacoes').addEventListener('click', async function () {
    const selects = document.querySelectorAll('.status-select');
    const alteracoes = [];

    selects.forEach(select => {
        alteracoes.push({
            idPessoa: select.getAttribute("data-id"),
            novoStatus: select.value
        });
    });

    try {
        const response = await fetch("http://localhost:8080/pessoas/solicitacoes", {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(alteracoes)
        });

        if (response.ok) {
            const modalElement = document.getElementById('modalSolicitacoes');
            const modal = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
            modal.hide();
        } else {
            console.log("Erro ao salvar alterações:");
        }
    } catch (error) {
        console.error("Erro ao enviar alterações:", error);
    }
});

async function abrirModalParticipante(participanteTabela) {
    document.getElementById('formParticipante').reset();
    document.getElementById('divMinisterio').classList.add('d-none');
    document.getElementById('dadosResponsavel').classList.add('d-none');
    document.getElementById('divInativar').classList.add('d-none');
    document.getElementById('idPessoa').value = participanteTabela?.idPessoa || "";

    if (participanteTabela) {
        document.getElementById('nome').value = participanteTabela.nome || "";
        document.getElementById('cpf').value = formatarCpf(participanteTabela.cpf || "");
        document.getElementById('telefone').value = formatarTelefone(participanteTabela.telefone || "");
        document.getElementById('dataNascimento').value = participanteTabela.dataNascimento || "";

        document.getElementById('divInativar').classList.remove('d-none');
        document.getElementById('coordenadorSelect').classList.remove('d-none');
        document.getElementById('modalParticipanteLabel').textContent = 'Editar Participante - ' + participanteTabela.nome;
    } else {
        document.getElementById('coordenadorSelect').classList.add('d-none');
    }

    if (participanteTabela?.idPessoa) {
        try {
            const response = await fetch(`http://localhost:8080/pessoas/detalhes/${participanteTabela.idPessoa}`);
            const detalhes = await response.json();

            document.getElementById('email').value = detalhes.email || "";
            document.getElementById('endereco').value = detalhes.endereco || "";
            document.getElementById('tipo').value = detalhes.tipo || "PARTICIPANTE";

            if (detalhes.tipo == "SERVO") {
                document.getElementById('divMinisterio').classList.remove('d-none');
                document.getElementById('ministerio').value = detalhes.ministerio || "";
            }

            if (detalhes.nomeResponsavel) {
                document.getElementById('dadosResponsavel').classList.remove('d-none');
                document.getElementById('nomeResponsavel').value = detalhes.nomeResponsavel || "";
                document.getElementById('cpfResponsavel').value = formatarCpf(detalhes.cpfResponsavel) || "";
                document.getElementById('telefoneResponsavel').value = formatarTelefone(detalhes.telefoneResponsavel) || "";
            }
            document.getElementById('observacao').value = detalhes.observacao || "";
        } catch (error) {
            console.error("Erro ao buscar detalhes do participante:", error);
        }
    }

    document.getElementById('tipo').addEventListener('change', function () {
        const divMinisterio = document.getElementById('divMinisterio');
        const tipoSelecionado = this.value;

        if (tipoSelecionado === 'SERVO') {
            divMinisterio.classList.remove('d-none');
        } else {
            divMinisterio.classList.add('d-none');
            document.getElementById('ministerio').value = "";
        }
    });
}

const form = document.getElementById('formParticipante');

form.addEventListener('submit', (e) => {
    const acao = e.submitter.dataset.acao;
    salvarOuAtualizarParticipante(acao === "salvar");
});

async function salvarOuAtualizarParticipante(ativo) {
    const idPessoa = document.getElementById('idPessoa').value;
    const ministerioValue = document.getElementById('ministerio').value;

    const nomeResp = document.getElementById('nomeResponsavel').value.trim();
    const cpfResp = document.getElementById('cpfResponsavel').value.trim();
    const telResp = document.getElementById('telefoneResponsavel').value.trim();

    const responsavel = (nomeResp || cpfResp || telResp)
        ? { nome: nomeResp, cpf: cpfResp, telefone: telResp }
        : null;

    const payload = {
        nome: document.getElementById('nome').value,
        cpf: limparFormatacao(document.getElementById('cpf').value),
        telefone: limparFormatacao(document.getElementById('telefone').value),
        email: document.getElementById('email').value,
        nascimento: document.getElementById('dataNascimento').value,
        endereco: document.getElementById('endereco').value,
        tipo: document.getElementById('tipo').value,
        ministerio: ministerioValue === "" ? null : ministerioValue,
        sexo: document.getElementById('sexo').value,
        observacao: document.getElementById('observacao').value,
        ativo: ativo,
        responsavel: responsavel
    };
    try {
        const url = idPessoa ? `http://localhost:8080/pessoas/${idPessoa}` : `http://localhost:8080/pessoas`;
        const method = idPessoa ? "PUT" : "POST";

        const response = await fetch(url, {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            location.reload();
        } else {
            console.log('Erro ao salvar participante.');
        }
    } catch (error) {
        console.error(error);
    }
}

async function carregarParticipantesInativos() {
    const tabela = document.getElementById('tabelaInativos');

    try {
        const response = await fetch('http://localhost:8080/pessoas/inativos');
        const inativos = await response.json();

        if (!inativos || inativos.length === 0) {
            tabela.innerHTML = `
                <tr>
                    <td colspan="4" class="text-center text-muted py-4">
                        Nenhum participante inativo encontrado.
                    </td>
                </tr>
            `;
            return;
        }

        tabela.innerHTML = "";

        inativos.forEach(inativo => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td class="text-center">${inativo.nome}</td>
                <td class="text-center">
                    <button class="btn btn-sm vermelho-border branco-hover" id="${inativo.idPessoa}" onclick="reativarPessoa(${inativo.idPessoa})">
                        <i class="bi bi-person-check"></i> Reativar
                    </button>
                </td>
            `;
            tabela.appendChild(tr);
        })
    } catch (error) {
        console.log('Erro ao buscar inativos. ', error);
    }
}

async function reativarPessoa(idPessoa) {
    const pessoa = {
        ativo: true
    };

    try {
        const response = await fetch(`http://localhost:8080/pessoas/${idPessoa}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(pessoa)
        });
    } catch (error) {
        console.error('Erro ao reativar participante. ', error);
    }
    carregarParticipantesInativos();
}












document.getElementById('telefone').addEventListener('input', function (e) {
    let valor = e.target.value.replace(/\D/g, '');
    valor = valor.substring(0, 11);

    if (valor.length > 2 && valor.length <= 10) {
        if (valor.length > 6) {
            valor = valor.replace(/^(\d{2})(\d{4})(\d{0,4})$/, '$1 $2-$3');
        } else {
            valor = valor.replace(/^(\d{2})(\d{0,4})$/, '$1 $2');
        }
    } else if (valor.length === 11) {
        valor = valor.replace(/^(\d{2})(\d{5})(\d{0,4})$/, '$1 $2-$3');
    }

    e.target.value = valor;
});

document.getElementById('pesquisarCpf').addEventListener('input', function (e) {
    e.target.value = formatarCpf(e.target.value);
});

document.getElementById('cpf').addEventListener('input', function (e) {
    e.target.value = formatarCpf(e.target.value);
});

document.getElementById('cpfResponsavel').addEventListener('input', function (e) {
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

function limparFormatacao(valor) {
    return valor.replace(/\D/g, '');
}
