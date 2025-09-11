const usuario = JSON.parse(localStorage.getItem('usuario'));

fetch('http://localhost:8080/encontros')
  .then(response => response.json())
  .then(data => {
    const container = document.getElementById('cardContainer');
    data.forEach(encontro => {
      const isCoordenador = usuario && usuario.coordenador === 'COORDENADOR';

      const botaoCancelar = isCoordenador
        ? `<button class="btn btn-danger btn-sm position-absolute top-0 end-0 m-2 btn-cancelar d-none">
       <i class="bi bi-x-lg"></i>
     </button>`
        : '';

      const card = `
  <div class="col">
    <div class="card h-100 position-relative card-encontro">
      <a href="visualizarEncontro.html?id=${encontro.id}" class="text-decoration-none">
        <img src="${encontro.capa}" class="card-img-top" alt="${encontro.titulo}">
        <div class="card-body">
          <h5 class="card-title text-dark">${encontro.titulo}</h5>
        </div>
      </a>
      ${botaoCancelar}
    </div>
  </div>
`;
      container.innerHTML += card;
    });

    document.querySelectorAll('.btn-cancelar').forEach(botao => {
      botao.addEventListener('click', function (e) {
        e.preventDefault();

        const parentLink = botao.closest('.position-relative').querySelector('a');
        const url = new URL(parentLink.href);
        encontroIdParaCancelar = url.searchParams.get('id');
        const titulo = botao.closest('.card').querySelector('.card-title').textContent;

        const modal = new bootstrap.Modal(document.getElementById('modalCancelar'));
        document.getElementById('modalCancelarLabel').textContent = titulo;
        modal.show();
      });
    });

    document.getElementById('confirmarCancelar').addEventListener('click', function () {
      if (encontroIdParaCancelar) {
        fetch(`http://localhost:8080/encontros/${encontroIdParaCancelar}`, {
          method: 'DELETE'
        })
          .then(response => {
            if (response.ok) {
              const card = document.querySelector(`a[href*="id=${encontroIdParaCancelar}"]`).closest('.col');
              card.remove();

              const modal = bootstrap.Modal.getInstance(document.getElementById('modalCancelar'));
              modal.hide();
            } else {
              console.log('Erro para excluir o encontro.');
            }
          })
          .catch(error => {
            console.log('Erro para excluir o encontro: ', error);
          });
      }
    });
  })
  .catch(error => console.error('Erro ao carregar os dados:', error));

function logar() {
    const paginaAtual = encodeURIComponent(window.location.href);
    window.location.href = `/html/login.html?redirect=${paginaAtual}`;
}

function sair() {
    localStorage.removeItem('usuario');
    window.location.reload(); 
}

if (usuario) {
  if (usuario.coordenador == 'COORDENADOR') {
    document.getElementById('btn-cadastrar').classList.remove('d-none');
    document.body.classList.add('coordenador');
  }
  document.body.classList.add('usuario-logado');
  document.getElementById('usuarioNome').innerHTML = usuario.nome;
  document.getElementById('usuarioNome').classList.remove('d-none');
  document.getElementById('btn-sair').classList.remove('d-none');
  document.querySelector('.btn-light').classList.add('d-none');
} else {
  document.body.classList.remove('usuario-logado');
}