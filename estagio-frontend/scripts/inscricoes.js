function voltarEncontro() {
    window.location.href = `/html/visualizarEncontro.html?id=${idEncontro}`;
}

const params = new URLSearchParams(window.location.search);
const idEncontro = parseInt(params.get("id"));

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

    const inscricoesResponse = await fetch(`http://localhost:8080/inscricoes/encontro/${idEncontro}`);
    const inscricoes = await inscricoesResponse.json();
    preencherListaInscricoes(inscricoes);
});

function preencherResumo(resumo) {
    document.getElementById("totalInscritos").innerText = resumo.total;
    document.getElementById("pagos").innerText = resumo.pagos;
    document.getElementById("naoPagos").innerText = resumo.naoPagos;
    document.getElementById("inscricoesServos").innerText = resumo.servos;
    document.getElementById("inscricoesParticipantes").innerText = resumo.participantes;
}

function preencherListaInscricoes(inscricoes) {
    if (inscricoes.length === 0) {
        document.getElementById('lista-vazio').classList.remove('d-none');
        return;
    }

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
            btnEmail.addEventListener('click', () => enviarEmail(inscricao.id));
        }
        tabela.appendChild(tr);
    });
}

async function carregardetalhesInscricao(inscricao) {
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
        document.getElementById("nomeResponsavel").value = detalhes.nomeResponsavel;
        document.getElementById("telefoneResponsavel").value = formatarTelefone(detalhes.telefoneResponsavel);
    }
}

async function enviarEmail(idInscricao) {
    console.log(".");
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
    telefone = telefone.replace(/\D/g, '');

    if (telefone.length === 11) {
        return telefone.replace(/^(\d{2})(\d{5})(\d{4})$/, '$1 $2-$3');
    } else if (telefone.length === 10) {
        return telefone.replace(/^(\d{2})(\d{4})(\d{4})$/, '$1 $2-$3');
    } else {
        return telefone;
    }
}
function formatarData(data) {
    const partesData = data.split('-');

    const ano = partesData[0];
    const mes = partesData[1];
    const dia = partesData[2];

    return `${dia}/${mes}/${ano}`;
}