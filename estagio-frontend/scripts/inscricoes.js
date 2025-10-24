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
    console.log(resumo);
    preencherResumo(resumo);

    const inscricoesResponse = await fetch(`http://localhost:8080/inscricoes/encontro/${idEncontro}`);
    const inscricoes = await inscricoesResponse.json();
    console.log(inscricoes);
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
}