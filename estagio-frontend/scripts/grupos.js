const usuario = JSON.parse(localStorage.getItem('usuario'));
function logar() {
  const paginaAtual = encodeURIComponent(window.location.href);
  window.location.href = `/html/login.html?redirect=${paginaAtual}`;
}

function sair() {
  window.location.href = '/html/encontros.html';
  localStorage.removeItem('usuario');
}

if (usuario) {
  document.getElementById('usuarioNome').innerHTML = usuario.nome;
  document.getElementById('usuarioNome').classList.remove('d-none');
  document.getElementById('btn-sair').classList.remove('d-none');
  document.querySelector('.btn-light').classList.add('d-none');
}

const params = new URLSearchParams(window.location.search);
const idEncontro = parseInt(params.get('id'));

function voltarEncontro() {
  window.location.href = `/html/visualizarEncontro.html?id=${idEncontro}`;
}

if (idEncontro) {
  fetch(`http://localhost:8080/encontros/${idEncontro}`)
    .then(response => response.json())
    .then(encontro => {
      document.getElementById("titulo").textContent = encontro.titulo;
    })
    .catch(error => console.error("Erro ao buscar encontro:", error));
}

window.addEventListener("DOMContentLoaded", carregarLideres);

lider1.addEventListener('change', atualizarOpcoesLider);
lider2.addEventListener('change', atualizarOpcoesLider);

async function carregarLideres() {
  const response = await fetch(`http://localhost:8080/pessoas/lideres/${idEncontro}`);
  const servos = await response.json();

  servos.forEach(servo => {
    let option1 = document.createElement('option');
    option1.value = servo.id;
    option1.text = servo.nome;

    let option2 = document.createElement('option');
    option2.value = servo.id;
    option2.text = servo.nome;

    lider1.appendChild(option1);
    lider2.appendChild(option2);
  });

  atualizarOpcoesLider();
}

function atualizarOpcoesLider() {
  const lider1 = document.getElementById('lider1');
  const lider2 = document.getElementById('lider2');
  const valor1 = lider1.value;
  const valor2 = lider2.value;

  Array.from(lider1.options).forEach(option => { option.disabled = false; });
  Array.from(lider2.options).forEach(option => { option.disabled = false; });

  if (valor1) {
    const option = lider2.querySelector(`option[value="${valor1}"]`);
    if (option) option.disabled = true;
  }

  if (valor2) {
    const option = lider1.querySelector(`option[value="${valor2}"]`);
    if (option) option.disabled = true;
  }
}

window.addEventListener("DOMContentLoaded", carregarGrupos);

async function carregarGrupos() {
  const containerVazio = document.getElementById('lista-vazio');
  const containerGrupos = document.getElementById('lista-grupos');

  try {
    const response = await fetch(`http://localhost:8080/grupos/${idEncontro}`);
    const grupos = await response.json();

    containerGrupos.innerHTML = '';
    if (grupos.length === 0) {
      containerVazio.classList.remove("d-none");
      containerGrupos.classList.add("d-none");
      return;
    }

    containerVazio.classList.add("d-none");
    containerGrupos.classList.remove("d-none");
    containerGrupos.classList.add("row", "g-3");

    grupos.forEach(grupo => {
      const lideres = grupo.lideres.map(lider => lider.nome).join(', ');
      const card = document.createElement('div');
      card.className = 'col-md-4';
      card.innerHTML = `
        <div class="card h-100" style="border-top: 7px solid ${grupo.cor};">
          <div class="card-header d-flex justify-content-between align-items-center">
            <h6 class="card-title mb-0">${grupo.nome}</h6>
            <div class="d-flex gap-2">
            <i class="bi bi-pencil-square fs-6 text-black" style="cursor: pointer;" onclick='abrirModalEditar(${JSON.stringify(grupo)})'></i>
            <i class="bi bi-trash fs-6 text-black" style="cursor: pointer;" onclick="modalExcluirGrupo(${grupo.id}, '${grupo.nome}')"></i>
            </div>
          </div>
          <div class="card-body">
            <p class="card-text"><strong>Líderes:</strong> ${lideres}</p>
            <p class="card-text">${grupo.quantidadeParticipantes} participantes</p>
          </div>
          <div class="card-footer text-center d-flex justify-content-around">
            <a class="btn btn-light btn-sm border" onclick="abrirModalVisualizarParticipantes(${grupo.id})">Visualizar Participantes</a>
            <a class="btn vermelho vermelho-hover text-white btn-sm border" onclick="abrirModalAdicionarParticipantes(${grupo.id})">Adicionar</a>
            
          </div>
        </div>
      `;
      containerGrupos.appendChild(card);
    });
  } catch (error) {
    console.error('Erro ao carregar grupos:', error);
    containerVazio.classList.remove("d-none");
    containerGrupos.classList.add("d-none");
  }
}

let idGrupoExcluir = null;
function modalExcluirGrupo(idgrupo, nomegrupo) {
  idGrupoExcluir = idgrupo;
  document.getElementById('modalExcluirLabel').textContent = nomegrupo;

  const modal = new bootstrap.Modal(document.getElementById('modalExcluir'));
  modal.show();
}

document.getElementById('confirmarExcluir').addEventListener('click', async function () {
  if (!idGrupoExcluir) return;

  try {
    const response = await fetch(`http://localhost:8080/grupos/${idGrupoExcluir}`, {
      method: 'DELETE'
    });

    if (response.ok) {
      bootstrap.Modal.getInstance(document.getElementById("modalExcluir")).hide();
      carregarGrupos();
    }
  } catch (error) {
    console.error('Erro ao excluir grupo:', error);
  }
});


const modalGrupo = document.getElementById("modalGrupo");
const modalTitulo = document.getElementById("modalGrupoLabel");
const salvarBtn = document.getElementById('btnSalvarGrupo');
const formGrupo = document.getElementById("formCadastroGrupo");

function abrirModalCriar() {
  modalTitulo.textContent = "Cadastrar novo grupo";
  salvarBtn.textContent = "Salvar";

  document.getElementById("nomeGrupo").value = "";
  document.getElementById("corGrupo").value = "#000000";
  document.getElementById("lider1").innerHTML = '<option value="" selected disabled>Selecione um líder</option>';
  document.getElementById("lider2").innerHTML = '<option value="" selected disabled>Selecione um líder</option>';

  carregarLideres();

  formGrupo.removeEventListener("submit", criarGrupo);
  formGrupo.onsubmit = (event) => {
    event.preventDefault();
    criarGrupo();
  };

  new bootstrap.Modal(modalGrupo).show();
}

function abrirModalEditar(grupo) {
  carregarLideres();
  modalTitulo.textContent = "Editar grupo";
  salvarBtn.textContent = "Atualizar";

  document.getElementById("nomeGrupo").value = grupo.nome;
  document.getElementById("corGrupo").value = grupo.cor;
  document.getElementById("lider1").innerHTML = "";
  document.getElementById("lider2").innerHTML = "";

  const option1 = document.createElement('option');
  const option2 = document.createElement('option');

  option1.text = grupo.lideres[0].nome;
  option1.value = grupo.lideres[0].id;
  lider1.appendChild(option1);

  option2.text = grupo.lideres[1].nome;
  option2.value = grupo.lideres[1].id;
  lider2.appendChild(option2);

  lider1.value = grupo.lideres[0].id;
  lider2.value = grupo.lideres[1].id;

  formGrupo.onsubmit = (event) => {
    event.preventDefault();
    editarGrupo(grupo.id);
  };

  new bootstrap.Modal(modalGrupo).show();
}

async function criarGrupo() {
  const nome = document.getElementById("nomeGrupo").value;
  const lider1 = parseInt(document.getElementById("lider1").value);
  const lider2 = parseInt(document.getElementById("lider2").value);
  idLideres = [lider1, lider2];
  const cor = document.getElementById("corGrupo").value;

  const grupo = { nome, cor, idEncontro, idLideres };

  try {
    const response = await fetch(`http://localhost:8080/grupos`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(grupo)
    });

    if (response.ok) {
      const modalInstance = bootstrap.Modal.getInstance(modalGrupo);
      modalInstance.hide();
      carregarGrupos();
    } else {
      const error = await response.json();
      console.error("Erro ao criar grupo:", error);
      alert("Erro ao criar grupo: " + error.message);
    }
  } catch (error) {
    console.error("Erro ao criar grupo:", error);
    alert("Erro ao criar grupo.");
  }
}

async function editarGrupo(idGrupo) {
  const nome = document.getElementById("nomeGrupo").value;
  const lider1 = parseInt(document.getElementById("lider1").value);
  const lider2 = parseInt(document.getElementById("lider2").value);
  const idLideres = [lider1, lider2];
  const cor = document.getElementById("corGrupo").value;

  const grupoAtualizado = { nome, cor, idLideres, idEncontro };

  try {
    const response = await fetch(`http://localhost:8080/grupos/${idGrupo}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(grupoAtualizado)
    });

    if (response.ok) {
      const modalInstance = bootstrap.Modal.getInstance(modalGrupo);
      modalInstance.hide();
      carregarGrupos();
    } else {
      const error = await response.json();
      console.error("Erro ao atualizar grupo:", error);
      alert("Erro ao atualizar grupo: " + error.message);
    }
  } catch (error) {
    console.error("Erro ao atualizar grupo:", error);
    alert("Erro ao atualizar grupo.");
  }
}

const modalAdicionarParticipantes = document.getElementById("modalAdicionarParticipantes");

async function abrirModalAdicionarParticipantes(idGrupo) {
  const response = await fetch(`http://localhost:8080/participantes-grupos/encontro/${idEncontro}`);
  const participantes = await response.json();

  const listaAdicionar = document.getElementById("listaAdicionarParticipantes");
  const campoPesquisa = document.getElementById("pesquisaParticipante");
  campoPesquisa.value = "";

  if (participantes.length === 0) {
    listaAdicionar.innerHTML = '<li class="list-group-item text-center">Nenhum participante disponível para adicionar.</li>';
    new bootstrap.Modal(modalAdicionarParticipantes).show();
    return;
  }

  function gerarLista(filtro = "") {
    listaAdicionar.innerHTML = "";

    participantes
      .filter(p => p.nome.toLowerCase().includes(filtro.toLowerCase()))
      .forEach(participante => {
        const item = document.createElement("li");
        item.className = "list-group-item d-flex justify-content-between align-items-center";

        const spanNome = document.createElement("span");
        spanNome.textContent = participante.nome;

        const botaoAdicionar = document.createElement("button");
        botaoAdicionar.textContent = "Adicionar";
        botaoAdicionar.className = "btn vermelho vermelho-hover text-white btn-sm";

        botaoAdicionar.onclick = () => {
          botaoAdicionar.disabled = true;
          adicionarParticipante(participante.id, idGrupo)
        };

        item.appendChild(spanNome);
        item.appendChild(botaoAdicionar);
        listaAdicionar.appendChild(item);
      });
  }
  gerarLista();

  campoPesquisa.oninput = () => gerarLista(campoPesquisa.value);

  new bootstrap.Modal(modalAdicionarParticipantes).show();
}

modalAdicionarParticipantes.addEventListener('hidden.bs.modal', () => {
  carregarGrupos();
});

async function adicionarParticipante(idParticipante, idGrupo) {
  try {
    const response = await fetch(`http://localhost:8080/participantes-grupos/${idParticipante}/${idGrupo}`, {
      method: "POST"
    });

    if (response.ok) {
      console.log("Participante adicionado com sucesso.");
    } else {
      const error = await response.json();
      console.error("Erro ao adicionar participante:", error);
    }
  } catch (error) {
    console.error("Erro ao adicionar participante:", error);
  }
}

const modalVisualizarParticipantes = document.getElementById("modalVisualizarParticipantes");
async function abrirModalVisualizarParticipantes(idGrupo) {
  const response = await fetch(`http://localhost:8080/participantes-grupos/${idGrupo}`)
  const participantes = await response.json();
  const textoNenhumParticipante = document.getElementById("mensagemNenhumParticipante");
  const boataoAdicionar = document.getElementById("botaoAdicionarModalVisualizar");
  textoNenhumParticipante.classList.add("d-none");
  boataoAdicionar.classList.add("d-none");

  const listaVisualizar = document.getElementById("listaVisualizarParticipantes");
  listaVisualizar.innerHTML = "";

  if (participantes.length === 0) {
    const item = document.createElement("li");
    item.className = "list-group-item";
    boataoAdicionar.setAttribute("onclick", `
  bootstrap.Modal.getInstance(document.getElementById('modalVisualizarParticipantes')).hide();
  abrirModalAdicionarParticipantes(${idGrupo});
`);
    boataoAdicionar.classList.remove("d-none");
    textoNenhumParticipante.classList.remove("d-none");
  }

  participantes.forEach(participante => {
    const item = document.createElement("li");
    item.className = "list-group-item d-flex justify-content-between align-items-center";
    const spanNome = document.createElement("span");
    spanNome.textContent = participante.nome;

    const iconeRemover = document.createElement("i");
    iconeRemover.className = "bi bi-x-lg text-danger";
    iconeRemover.style.cursor = "pointer";
    iconeRemover.onclick = async () => removerParticipante(participante.id, idGrupo);

    item.appendChild(spanNome);
    item.appendChild(iconeRemover);

    listaVisualizar.appendChild(item);
  });

  const modal = bootstrap.Modal.getOrCreateInstance(modalVisualizarParticipantes);
  modal.show();
}

async function removerParticipante(idParticipante, idGrupo) {
  try {
    const response = await fetch(`http://localhost:8080/participantes-grupos/${idParticipante}/${idGrupo}`, {
      method: "DELETE"
    });
    if (response.ok) {
      abrirModalVisualizarParticipantes(idGrupo);
      carregarGrupos();
    }
  }
  catch (error) {
    console.error("Erro ao remover participante:", error);
  }
}