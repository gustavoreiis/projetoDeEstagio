function logar() {
  const paginaAtual = encodeURIComponent(window.location.href);
  window.location.href = `/html/login.html?redirect=${paginaAtual}`;
}

function sair() {
  localStorage.removeItem("token");
  window.location.href = '/html/encontros.html';
}

function getUsuarioLogado() {
  const token = localStorage.getItem("token");
  if (!token) return null;

  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload;
  } catch (e) {
    console.error("Token inválido:", e);
    return null;
  }
}

function verificarAutenticacao() {
  const token = localStorage.getItem("token");

  if (!token) {
    window.location.href = "/html/login.html?redirect=" + encodeURIComponent(window.location.pathname);
    return;
  }

  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    const agora = Date.now() / 1000;

    if (payload.exp && payload.exp < agora) {
      localStorage.removeItem("token");
      window.location.href = "/html/login.html";
    }

  } catch (e) {
    console.error("Token inválido:", e);
    localStorage.removeItem("token");
    window.location.href = "/html/login.html";
  }
}

document.addEventListener('DOMContentLoaded', () => {
  const token = localStorage.getItem('token');
  const btnSair = document.getElementById("btn-sair");
  const btnLogin = document.querySelector(".btn-light");
  const usuarioNome = document.getElementById("usuarioNome");

  if (!token) {
    btnSair.classList.add("d-none");
    btnLogin.classList.remove("d-none");
    usuarioNome.textContent = "Coordenador";
  } else {
    const payload = JSON.parse(atob(token.split(".")[1]));
    const nome = payload.nome;
    usuarioNome.textContent = nome;
    btnSair.classList.remove("d-none");
    btnLogin.classList.add("d-none");
  }
})