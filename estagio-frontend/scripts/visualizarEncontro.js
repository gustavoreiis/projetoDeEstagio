const params = new URLSearchParams(window.location.search);
const id = params.get('id');
document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('btn-inscricao').href = `inscricao.html?encontro=${id}`;
  if (!id) {
    window.location.href = "encontros.html";
    return;
  }
})

function abrirInscricoes() {
  const id = params.get('id');
  window.location.href = `inscricoes.html?id=${id}`;
}

function abrirGrupos() {
  const id = params.get('id');
  window.location.href = `grupos.html?id=${id}`;
}

function formatarDataBR(date) {
  const d = new Date(date);
  const dia = String(d.getDate()).padStart(2, '0');
  const mes = String(d.getMonth() + 1).padStart(2, '0');
  const ano = d.getFullYear();
  const hora = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  return `${dia}/${mes}/${ano} ${hora}:${min}`;
}

fetch(`http://localhost:8080/encontros/${id}`)
  .then(res => res.json())
  .then(encontro => {
    const dataHoraInicioFormatada = formatarDataBR(encontro.dataHoraInicio);
    const dataHoraFimFormatada = formatarDataBR(encontro.dataHoraFim);
    document.getElementById('imagem-encontro').src = encontro.capa;
    document.getElementById('titulo-encontro').textContent = encontro.titulo;
    document.getElementById('descricao-encontro').textContent = encontro.descricao;
    document.getElementById('data-inicio-encontro').textContent = dataHoraInicioFormatada;
    document.getElementById('data-fim-encontro').textContent = "até " + dataHoraFimFormatada;
    document.getElementById('local-encontro').textContent = encontro.local;
    let precoFormatado = encontro.preco.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    document.getElementById('preco-encontro').textContent = "R$ " + precoFormatado;
    document.getElementById('botao-editar').setAttribute('onclick', `editarEncontro(${encontro.id})`);
  })
  .catch(err => console.error('Erro ao carregar detalhes do encontro:', err));

function editarEncontro(id) {
  window.location.href = `cadastrarEditarEncontro.html?id=${id}`;
}

const token = localStorage.getItem('token');

if (token) {
  document.getElementById('hr-acoes').classList.remove('d-none');
  document.getElementById('acoes-encontro').classList.remove('d-none');
} else {
  document.getElementById('hr-acoes').classList.add('d-none');
  document.getElementById('acoes-encontro').classList.add('d-none');
}







