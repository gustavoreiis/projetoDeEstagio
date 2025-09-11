document.getElementById("loginForm").addEventListener("submit", function(e) {
        e.preventDefault();

        const cpf = limparFormatacaoCpf(document.getElementById("cpf").value);
        const senha = document.getElementById("senha").value;

        const urlParams = new URLSearchParams(window.location.search);
        const redirect = urlParams.get("redirect") || "encontros.html";

        fetch("http://localhost:8080/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ cpf, senha })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao fazer login");
            }
            return response.json();
        })
        .then(data => {
            localStorage.setItem("usuario", JSON.stringify(data));
            window.location.href = redirect;
        })
        .catch(error => {
            console.error("Erro:", error);
            document.getElementById("loginError").classList.remove("d-none");
        });
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