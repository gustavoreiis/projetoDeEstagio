verificarAutenticacao();

function voltarEncontro() {
    window.location.href = `/html/visualizarEncontro.html?id=${idEncontro}`;
}

const params = new URLSearchParams(window.location.search);
const idEncontro = parseInt(params.get("id"));
document.addEventListener('DOMContentLoaded', () => {
    if (!idEncontro) {
        window.location.href = "encontros.html";
        return;
    }
})

if (idEncontro) {
    fetch(`http://localhost:8080/encontros/${idEncontro}`)
        .then(response => response.json())
        .then(encontro => {
            document.getElementById("titulo").innerText = encontro.titulo;
        })
        .catch(error => {
            console.error("Erro ao buscar dados do encontro:", error);
        });
}

document.addEventListener("DOMContentLoaded", async function () {
    const response = await fetch(`http://localhost:8080/inscricoes/encontro/${idEncontro}/resumo`);
    const resumo = await response.json();
    preencherResumo(resumo);
    carregarInscricoes();
});

function preencherResumo(resumo) {
    document.getElementById("totalInscritos").innerText = resumo.total;
    document.getElementById("pagos").innerText = resumo.pagos;
    document.getElementById("naoPagos").innerText = resumo.naoPagos;
    document.getElementById("inscricoesServos").innerText = resumo.servos;
    document.getElementById("inscricoesParticipantes").innerText = resumo.participantes;
}

document.getElementById("btnPesquisar").addEventListener("click", carregarInscricoes);
async function carregarInscricoes() {
    const nome = document.getElementById('pesquisarNome').value.trim();
    const grupo = document.getElementById('tipoFiltro').value;
    const pagamento = document.querySelector('input[name="pagamentoFiltro"]:checked')?.value;

    const params = new URLSearchParams();
    if (nome) params.append('nome', nome);
    if (grupo) params.append('grupo', grupo);
    if (pagamento) params.append('pagamento', pagamento);
    console.log(params.toString());

    try {
        const response = await fetch(`http://localhost:8080/inscricoes/encontro/${idEncontro}?` + params.toString());

        if (!response.ok) {
            throw new Error('Erro ao carregar inscrições');
        }

        const inscricoes = await response.json();
        const tabela = document.getElementById('tabelaInscritos');
        tabela.innerHTML = '';
        preencherListaInscricoes(inscricoes);
    } catch (error) {
        console.error('Erro ao carregar inscrições:', error);
        mostrarToast('Erro', 'Não foi possível carregar as inscrições.');
    }
}

function preencherListaInscricoes(inscricoes) {
        if (inscricoes.length === 0) {
            document.getElementById('lista-vazio').classList.remove('d-none');
            return;
        }
        document.getElementById('lista-vazio').classList.add('d-none');

        const tabela = document.getElementById('tabelaInscritos');
        inscricoes.forEach(inscricao => {
            const tr = document.createElement('tr');
            const badge = inscricao.pago === 'Concluído' ? 'bg-success' : 'bg-secondary';
            tr.innerHTML = `
            <td class="text-center">${inscricao.nome}</td>
            <td class="text-center">${formatarTelefone(inscricao.telefone)}</td>
            <td class="text-center">${inscricao.grupo}</td>
            <td class="text-center"><span class="badge ${badge}">${inscricao.pago}</span></td>
            <td class="text-center">${formatarData(inscricao.dataNascimento)}</td>
            <td class="text-center">
                <button class="btn vermelho-border" data-bs-toggle="modal" data-bs-target="#modalDetalhes">
                    <i class="bi bi-eye"></i>
                </button>
                <button id="enviarEmail${inscricao.id}" class="btn vermelho-border" title="Reenviar e-mail de pagamento">
                    <i class="bi bi-envelope"></i>
                </button>
            </td>
        `;

            const btnDetalhes = tr.querySelector('button[data-bs-target="#modalDetalhes"]');
            btnDetalhes.addEventListener('click', () => carregardetalhesInscricao(inscricao));

            const btnEmail = tr.querySelector(`#enviarEmail${inscricao.id}`);
            if (inscricao.pago === 'Concluído') {
                btnEmail.style.opacity = '0.5';
                btnEmail.addEventListener('click', () => {
                    mostrarToast('Inscrição já paga!', 'Este participante já realizou o pagamento.');
                });
            } else {
                btnEmail.addEventListener('click', () => {
                    inscricaoSelecionadaId = inscricao.id;
                    const modal = new bootstrap.Modal(document.getElementById('confirmarEnvioModal'));
                    modal.show();
                }
                )
            };
            tabela.appendChild(tr);
        });
    }

    async function carregardetalhesInscricao(inscricao) {
        document.getElementById("formDetalhes").dataset.idInscricao = inscricao.id;

        document.getElementById("nomeInscrito").value = inscricao.nome;
        document.getElementById("telefoneInscrito").value = formatarTelefone(inscricao.telefone);
        document.getElementById("grupoInscrito").value = inscricao.grupo;
        document.getElementById("dataNascimentoInscrito").value = formatarData(inscricao.dataNascimento);
        document.getElementById("pagamentoInscrito").value = inscricao.pago;

        const response = await fetch(`http://localhost:8080/inscricoes/${inscricao.id}`)
        const detalhes = await response.json();

        document.getElementById("emailInscrito").value = detalhes.email;
        document.getElementById("dataInscricao").value = formatarData(detalhes.dataInscricao);
        document.getElementById("enderecoInscrito").value = detalhes.endereco;
        document.getElementById("autorizacaoInscrito").value = detalhes.statusAutorizado;
        document.getElementById("observacaoInscrito").value = detalhes.observacao;

        if (detalhes.nomeResponsavel == null) {
            document.getElementById("camposResponsavel").classList.add("d-none");
        } else {
            document.getElementById("camposResponsavel").classList.remove("d-none");
            document.getElementById("nomeResponsavel").value = detalhes.nomeResponsavel;
            document.getElementById("telefoneResponsavel").value = formatarTelefone(detalhes.telefoneResponsavel);
        }

        const container = document.getElementById("arquivoAutorizacaoContainer");
        container.innerHTML = "";

        if (detalhes.autorizacao) {

            const link = document.createElement("a");
            link.href = detalhes.autorizacao;
            link.target = "_blank";
            link.classList.add("btn", "branco-hover", "btn-sm");
            link.innerHTML = '<i class="bi bi-eye"></i> Visualizar Autorização';

            container.appendChild(link);
        } else {
            container.innerHTML = `<p class="text-muted mb-0 small">Nenhum arquivo anexado.</p>`;
        }
    }

    document.getElementById('salvarAlteracoes').addEventListener('click', async function () {
        const form = document.getElementById("formDetalhes");
        const idInscricao = form.dataset.idInscricao;

        if (!idInscricao) {
            mostrarToast("Erro", "Nenhuma inscrição selecionada.");
            return;
        }

        const dadosAtualizados = {
            pago: document.getElementById("pagamentoInscrito").value === "Concluído",
            autorizado: document.getElementById("autorizacaoInscrito").value === "Autorizado",
            observacao: document.getElementById("observacaoInscrito").value
        };

        try {
            const response = await fetch(`http://localhost:8080/inscricoes/${idInscricao}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dadosAtualizados)
            });

            if (response.ok) {
                mostrarToast("Sucesso", "Alterações salvas com sucesso.");
                const modal = bootstrap.Modal.getInstance(document.getElementById("modalDetalhes"));
                modal.hide();
                window.location.reload();
            } else {
                const erro = await response.text();
                mostrarToast("Erro", `Falha ao salvar alterações: ${erro}`);
            }
        } catch (error) {
            mostrarToast("Erro", `Erro ao salvar alterações: ${error.message}`);
        }
    });

    document.getElementById('confirmarEnvioEmail').addEventListener('click', async function () {
        if (!inscricaoSelecionadaId) return;

        const modal = bootstrap.Modal.getInstance(document.getElementById("confirmarEnvioModal"));
        modal.hide();

        await enviarEmail(inscricaoSelecionadaId);
        mostrarToast("E-mail enviado com sucesso!", "O e-mail de pagamento foi reenviado ao inscrito.");

        inscricaoSelecionadaId = null;
    });

    async function enviarEmail(idInscricao) {
        const response = await fetch(`http://localhost:8080/inscricoes/email/${idInscricao}`);
        const emailEnviado = response.json();
    }

    function mostrarToast(titulo, mensagem) {
        const toastContainer = document.getElementById('toastContainer');
        toastContainer.innerHTML = `
    <div class="toast align-items-center text-bg-secondary border-0" role="alert" aria-live="assertive" aria-atomic="true">
      <div class="d-flex">
        <div class="toast-body">
          <strong>${titulo}</strong><br>${mensagem}
        </div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
      </div>
    </div>
  `;
        const toast = new bootstrap.Toast(toastContainer.querySelector('.toast'));
        toast.show();
    }

    function formatarTelefone(telefone) {
        if (!telefone) return "-";
        telefone = telefone.replace(/\D/g, '');
        if (telefone.length === 11) return `(${telefone.slice(0, 2)}) ${telefone.slice(2, 7)}-${telefone.slice(7)}`;
        if (telefone.length === 10) return `(${telefone.slice(0, 2)}) ${telefone.slice(2, 6)}-${telefone.slice(6)}`;
        return telefone;
    }

    function formatarData(data) {
        const partesData = data.split('-');

        const ano = partesData[0];
        const mes = partesData[1];
        const dia = partesData[2];

        return `${dia}/${mes}/${ano}`;
    }