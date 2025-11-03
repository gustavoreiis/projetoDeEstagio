verificarAutenticacao();

function previewImage(event) {
    const input = event.target;
    const preview = document.getElementById('preview');
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
            preview.style.display = 'block'; 
        };
        reader.readAsDataURL(input.files[0]);
    } else {
        preview.src = '';
        preview.style.display = 'none';
    }
}

window.addEventListener('DOMContentLoaded', function() {
  const params = new URLSearchParams(window.location.search);
  const idEncontro = params.get('id');

  if (idEncontro) {
    carregarEncontroParaEdicao(idEncontro);
  }

  const agora = new Date();
    const offset = agora.getTimezoneOffset();
    const agoraLocal = new Date(agora.getTime() - (offset * 60000))
                         .toISOString()
                         .slice(0,16);

    document.getElementById('dataHoraInicio').setAttribute('min', agoraLocal);
    document.getElementById('dataHoraFim').setAttribute('min', agoraLocal);

    this.document.getElementById('dataHoraInicio').addEventListener('change', function() {
        const dataHoraInicio = new Date(this.value);
        const dataHoraFimInput = document.getElementById('dataHoraFim');
        dataHoraFimInput.setAttribute('min', this.value);
    });
});

function carregarEncontroParaEdicao(idEncontro) {
  fetch(`http://localhost:8080/encontros/${idEncontro}`)
    .then(response => response.json())
    .then(encontro => {
      document.getElementById('idEncontro').value = encontro.idEncontro;
      document.getElementById('titulo').value = encontro.titulo;
      document.getElementById('descricao').value = encontro.descricao;
      document.getElementById('dataHoraInicio').value = encontro.dataHoraInicio;
      document.getElementById('dataHoraFim').value = encontro.dataHoraFim;
      document.getElementById('local').value = encontro.local;
      document.getElementById('preco').value = encontro.preco;
      document.getElementById('titulo-form').innerHTML = "Editar Encontro";
      document.getElementById('capa').removeAttribute('required');
      const preview = document.getElementById('preview');
      preview.src = encontro.capa;
      preview.style.display = 'block';
    })
    .catch(error => console.error('Erro ao carregar o encontro:', error));
}

function salvarEncontro(event) {
    event.preventDefault();

    const dataHoraInicio = new Date(document.getElementById("dataHoraInicio").value);
    const dataHoraFim = new Date(document.getElementById("dataHoraFim").value);

    if (dataHoraFim < dataHoraInicio) {
        mostrarErroData("A data de encerramento não pode ser anterior à data de início. Revise as informações e tente novamente.");
        return;
    }

    const params = new URLSearchParams(window.location.search);
    const idEncontro = params.get("id");

    const formData = new FormData();

    formData.append("titulo", document.getElementById("titulo").value);
    formData.append("descricao", document.getElementById("descricao").value);
    formData.append("dataHoraInicio", document.getElementById("dataHoraInicio").value);
    formData.append("dataHoraFim", document.getElementById("dataHoraFim").value);
    formData.append("local", document.getElementById("local").value);
    formData.append("preco", document.getElementById("preco").value);

    const inputCapa = document.getElementById("capa");
    if (inputCapa.files && inputCapa.files.length > 0) {
        formData.append("capa", inputCapa.files[0]);
    }

    const url = idEncontro
        ? `http://localhost:8080/encontros/${idEncontro}`
        : "http://localhost:8080/encontros";

    const method = idEncontro ? "PUT" : "POST";
    fetch(url, {
        method: method,
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Erro ao salvar o encontro.");
        }
        return response.text();
    })
    .then(() => {
        window.location.href = "encontros.html";
    })
    .catch(error => {
        console.error("Erro ao salvar o encontro:", error);
        alert("Erro ao salvar o encontro.");
    });
}

function mostrarErroData(mensagem) {
    const mensagemErroData = document.getElementById("mensagemErroData");
    mensagemErroData.textContent = mensagem;

    const modalErroData = new bootstrap.Modal(document.getElementById("modalErroData"));
    modalErroData.show();
}

document.getElementById("form-encontro").addEventListener("submit", salvarEncontro);


