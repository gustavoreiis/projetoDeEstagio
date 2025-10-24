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
    if (inscricoes.length == 0) {
        document.getElementById('lista-vazio').classList.remove('d-none');
        return;
    }

    const tabela = document.getElementById('tabelaInscritos');
    inscricoes.forEach(inscricao => {
        let badge;
        if (inscricao == 'Concluído') {
            badge = 'bg-success';
        } else {
            badge = 'bg-secondary';
        }
        const item = `
            <tr>
                <td class="text-center">${inscricao.nome}</td>
                <td class="text-center">${formatarTelefone(inscricao.telefone)}</td>
                <td class="text-center">${inscricao.grupo}</td>
                <td class="text-center"><span class="badge ${badge}">${inscricao.pago}</span></td>
                <td class="text-center">${formatarData(inscricao.dataNascimento)}</td>
                <td class="text-center">
                    <button class="btn vermelho-border" data-bs-toggle="modal" data-bs-target="#modalDetalhes"><i class="bi bi-eye"></i></button>
                    <button class="btn vermelho-border" title="Reenviar e-mail de pagamento"><i class="bi bi-envelope"></i></button>
                </td>
            </tr>
        `
        tabela.innerHTML += item;
    });
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


/*
<tr>
    <td>Maria Silva</td>
    <td>123.456.789-00</td>
    <td>(11) 99999-9999</td>
    <td>Participante</td>
    <td><span class="badge bg-success">Concluído</span></td>
    <td>12/10/2025</td>
    <td>
        <button class="btn vermelho-border" data-bs-toggle="modal"
            data-bs-target="#modalDetalhes"><i class="bi bi-eye"></i></button>
        <button class="btn vermelho-border" title="Reenviar e-mail de pagamento"><i
                class="bi bi-envelope"></i></button>
    </td>
</tr>*/