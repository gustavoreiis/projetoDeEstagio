document.getElementById("loginForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const cpf = limparFormatacaoCpf(document.getElementById("cpf").value);
  const senha = document.getElementById("senha").value;
  const urlParams = new URLSearchParams(window.location.search);
  const redirect = urlParams.get("redirect") || "encontros.html";
  const erroDiv = document.getElementById('loginError');

  try {
    const response = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ cpf, senha })
    });

    if (!response.ok) {
      const errorText = await response.text();
      
      erroDiv.textContent = errorText || "CPF ou senha inválidos.";
      erroDiv.classList.remove('d-none');
      setTimeout(() => {
        erroDiv.classList.add('d-none');
      }, 3000);

      throw new Error("Credenciais inválidas");
    }

    const data = await response.json();
    localStorage.setItem("token", data.token);
    window.location.href = redirect;

  } catch (error) {
    console.error("Erro ao fazer login:", error);
  }
});

const cpfInput = document.getElementById('cpf');

cpfInput.addEventListener('input', function (e) {
  let valor = e.target.value;

  valor = valor.replace(/\D/g, '');
  valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
  valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
  valor = valor.replace(/(\d{3})(\d{1,2})$/, '$1-$2');

  e.target.value = valor;
});

function limparFormatacaoCpf(cpf) {
  return cpf.replace(/\D/g, '');
}