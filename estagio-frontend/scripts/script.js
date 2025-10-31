document.addEventListener("DOMContentLoaded", async () => {
  const token = localStorage.getItem("token");
  const btnCadastrar = document.getElementById("btn-cadastrar");
  const btnSair = document.getElementById("btn-sair");
  const btnLogin = document.querySelector(".btn-light");
  const usuarioNome = document.getElementById("usuarioNome");

  if (!token) {
    btnLogin.classList.remove("d-none");
    btnSair.classList.add("d-none");
    usuarioNome.textContent = "Coordenador";
  } else {
    const payload = JSON.parse(atob(token.split(".")[1]));
    const nome = payload.nome;
    usuarioNome.textContent = nome;

    btnLogin.classList.add("d-none");
    btnSair.classList.remove("d-none");
    btnCadastrar.classList.remove("d-none");
  }

  window.sair = function () {
    localStorage.removeItem("token");
    window.location.reload();
  };

  window.logar = function () {
    const paginaAtual = encodeURIComponent(window.location.href);
    window.location.href = `/html/login.html?redirect=${paginaAtual}`;
  };

  try {
    const response = await fetch("http://localhost:8080/encontros", {
      headers: token ? { "Authorization": `Bearer ${token}` } : {}
    });

    if (!response.ok) {
      if (response.status === 401) {
        localStorage.removeItem("token");
        window.location.href = "/html/login.html";
        return;
      }
      throw new Error("Erro ao buscar encontros");
    }

    const data = await response.json();
    const container = document.getElementById("cardContainer");

    data.forEach(encontro => {
      const isLogado = !!token;

      const card = `
        <div class="col">
          <div class="card h-100 position-relative card-encontro">
            <a href="visualizarEncontro.html?id=${encontro.id}" class="text-decoration-none">
              <img src="${encontro.capa}" class="card-img-top" alt="${encontro.titulo}">
              <div class="card-body">
                <h5 class="card-title text-dark">${encontro.titulo}</h5>
              </div>
            </a>
            ${isLogado ? `<button class="btn btn-danger btn-sm btn-cancelar branco-hover">
              <i class="bi bi-x-lg"></i>
            </button>` : ""}
          </div>
        </div>`;
      container.innerHTML += card;
    });

    document.querySelectorAll(".btn-cancelar").forEach(botao => {
      botao.addEventListener("click", async e => {
        e.preventDefault();
        const parentLink = botao.closest(".position-relative").querySelector("a");
        const url = new URL(parentLink.href);
        const encontroId = url.searchParams.get("id");
        const titulo = botao.closest(".card").querySelector(".card-title").textContent;

        const modal = new bootstrap.Modal(document.getElementById("modalCancelar"));
        document.getElementById("modalCancelarLabel").textContent = titulo;
        modal.show();

        document.getElementById("confirmarCancelar").onclick = async function () {
          const delResponse = await fetch(`http://localhost:8080/encontros/${encontroId}`, {
            method: "DELETE",
            headers: { "Authorization": `Bearer ${token}` }
          });

          if (delResponse.ok) {
            botao.closest(".col").remove();
            modal.hide();
          } else {
            alert("Erro ao cancelar o encontro.");
          }
        };
      });
    });
  } catch (error) {
    console.error("Erro ao carregar encontros:", error);
  }
});
