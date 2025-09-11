const formVerificar = document.getElementById('form-verificar');
const formNovo = document.getElementById('form-novo');
const formSenha = document.getElementById('form-senha');
const jaCadastrado = document.getElementById('ja-cadastrado');
const nomeExistente = document.getElementById('nomeExistente');
const erroDiv = document.getElementById('erro');

formVerificar.addEventListener('submit', async (e) => {
    e.preventDefault();
    const cpf = limparFormatacao(document.getElementById('cpf').value);
    const nascimento = document.getElementById('nascimento').value;

    formNovo.classList.add('d-none');
    jaCadastrado.classList.add('d-none');
    erroDiv.classList.add('d-none');
    formSenha.classList.add('d-none');

    try {
        const res = await fetch(`http://localhost:8080/auth/verificar-cpf?cpf=${cpf}&nascimento=${nascimento}`);
        
        if (!res.ok) {
            const erro = await res.json();
            erroDiv.textContent = erro.erro;
            erroDiv.classList.remove('d-none');
            return;
        }

        const dados = await res.json();

        if (dados.erro) {
            erroDiv.classList.remove('d-none');
            erroDiv.textContent = dados.erro;
        } else if (!dados.existe) {
            mostrarEtapa('form-novo');
        } else if (!dados.temLogin) {
            nomeExistente.textContent = dados.nome;
            mostrarEtapa('form-senha');
        } else {
            jaCadastrado.classList.remove('d-none');
        }

    } catch (err) {
        console.error(err);
    }
});


document.getElementById('form-novo').addEventListener('submit', async (e) => {
    e.preventDefault();
    const cpf = limparFormatacao(document.getElementById('cpf').value);
    const nascimento = document.getElementById('nascimento').value;
    const nome = document.getElementById('nome').value;
    const senha = document.getElementById('senhaNovo').value;
    const email = document.getElementById('email').value;
    const telefone = limparFormatacao(document.getElementById('telefone').value);

    if (!telefoneValido(telefone)) {
        erroDiv.textContent = "Formato de telefone inválido.";
        erroDiv.classList.remove('d-none');
        return;
    }

    const resposta = await fetch('http://localhost:8080/auth/cadastro', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ cpf, nascimento, nome, senha, email, telefone })
    });
    if (resposta.ok) {
        window.location.href = 'login.html';
    }
});

document.getElementById('form-senha').addEventListener('submit', async (e) => {
    e.preventDefault();
    const cpf = limparFormatacao(document.getElementById('cpf').value);
    const senha = document.getElementById('senha').value;
    const resposta = await fetch('http://localhost:8080/auth/cadastrar-senha', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ cpf, senha })
    });
    if (resposta.ok) {
        window.location.href = 'login.html';
    }
});

function mostrarEtapa(etapaId) {
  const etapas = ['form-verificar', 'form-novo', 'form-senha'];
  etapas.forEach(id => {
    const el = document.getElementById(id);
    if (el) {
      if (id === etapaId) {
        el.classList.remove('d-none');
      } else {
        el.classList.add('d-none');
      }
    }
  });
}

const cpfInput = document.getElementById('cpf');

cpfInput.addEventListener('input', function (e) {
  let valor = e.target.value;

  valor = valor.replace(/\D/g, '');
  valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
  valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
  valor = valor.replace(/(\d{3})(\d{1,2})$/, '$1-$2');

  e.target.value = valor;
});

const telefoneInput = document.getElementById('telefone');

telefoneInput.addEventListener('input', function (e) {
  let valor = e.target.value.replace(/\D/g, '');
  valor = valor.substring(0, 11);

  if (valor.length > 2 && valor.length <= 10) {
    if (valor.length > 6) {
      valor = valor.replace(/^(\d{2})(\d{4})(\d{0,4})$/, '$1 $2-$3');
    } else {
      valor = valor.replace(/^(\d{2})(\d{0,4})$/, '$1 $2');
    }
  } else if (valor.length === 11) {
    valor = valor.replace(/^(\d{2})(\d{5})(\d{0,4})$/, '$1 $2-$3');
  }

  e.target.value = valor;
});

function limparFormatacao(valor) {
  return valor.replace(/\D/g, '');
}

function telefoneValido(telefone) {
  const numeros = telefone.replace(/\D/g, '');
  return numeros.length === 10 || numeros.length === 11;
}
