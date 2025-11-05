const modal = new bootstrap.Modal(document.getElementById('modalIdentificacao'));
window.addEventListener('load', () => {
});

document.getElementById('modalForm').addEventListener('submit', function (e) {
  e.preventDefault();

  let cpf = document.getElementById('modalCpf').value;
  const nascimento = document.getElementById('modalNascimento').value;
  cpf = limparFormatacao(cpf);
  verificarCpf(cpf, nascimento);
});

const erroDiv = document.getElementById('erro');

const encontroId = new URLSearchParams(window.location.search).get('encontro');
document.addEventListener('DOMContentLoaded', async () => {
  if (!encontroId) {
    window.location.href = "encontros.html";
    return;
  } else {
    const response = await fetch(`http://localhost:8080/encontros/${encontroId}`);
    const encontro = await response.json();
    const agora = new Date();
    const dataHoraFimEncontro = new Date(encontro.dataHoraFim);
    if (agora > dataHoraFimEncontro) {
      mostrarErroModal("As inscrições para este encontro estão encerradas. Redirecionando...");
      setTimeout(() => {
        window.location.href = "encontros.html";
      }, 3000);
    } else {
      modal.show();
    }
  }
})

async function verificarCpf(cpf, nascimento) {
  try {
    const res = await fetch(`http://localhost:8080/auth/verificar-cpf?cpf=${cpf}&nascimento=${nascimento}&encontroId=${encontroId}`);
    const dados = await res.json();

    if (!res.ok || dados.erro) {
      erroDiv.textContent = dados.erro;
      erroDiv.classList.remove('d-none');
      return;
    }
    document.getElementById('cpf').value = formatarCpf(cpf);
    document.getElementById('nascimento').value = nascimento;
    document.getElementById('cpf').disabled = true;

    if (dados.existe) {
      const resPessoa = await fetch(`http://localhost:8080/pessoas/cpf/${cpf}`);
      if (!resPessoa.ok) {
        console.error('Erro ao buscar dados da pessoa');
        return;
      }

      const pessoa = await resPessoa.json();

      document.getElementById('nome').value = pessoa.nome;
      document.getElementById('telefone').value = pessoa.telefone;
      document.getElementById('telefone').dispatchEvent(new Event('input'));
      document.getElementById('email').value = pessoa.email;
      document.getElementById('endereco').value = pessoa.endereco;
      document.getElementById('nascimento').value = pessoa.nascimento;
      document.getElementById('genero').value = pessoa.genero;
      document.getElementById('observacao').value = pessoa.observacao;
      document.getElementById('tipo').value = pessoa.tipo;
      document.getElementById('tipo').dispatchEvent(new Event('change'));
      document.getElementById('ministerio').value = pessoa.ministerio;
      document.getElementById('responsavelNome').value = pessoa.responsavel ? pessoa.responsavel.nome : '';
      document.getElementById('responsavelCpf').value = pessoa.responsavel ? pessoa.responsavel.cpf : '';
      document.getElementById('responsavelTelefone').value = pessoa.responsavel ? pessoa.responsavel.telefone : '';
    }
    acionarCamposResponsavel();
    modal.hide();

  } catch (err) {
    console.error('Erro ao verificar CPF:', err);
  }
}


document.getElementById('tipo').addEventListener('change', function () {
  if (this.value === 'SERVO') {
    document.getElementById('campos-servo').classList.remove('d-none');
    document.getElementById('ministerio').required = true;
  } else {
    document.getElementById('campos-servo').classList.add('d-none');
    document.getElementById('ministerio').required = false;
  }
});

function inscrever(dados) {
  fetch("http://localhost:8080/inscricoes", {
    method: "POST",
    body: dados
  })
    .then(response => {
      if (!response.ok) {
        return response.text().then(msg => { throw new Error(msg); });
      }
      return response.json();
    })
    .then(data => {
      mostrarToastSucesso("Inscrição realizada com sucesso!");
    })
    .catch(error => {
      mostrarErroModal("Erro ao realizar inscrição: " + error.message);
    });
}

document.getElementById('form-inscricao').addEventListener('submit', function (e) {
  e.preventDefault();

  if (!telefoneValido(document.getElementById('telefone').value)) {
    mostrarErroModal("Formato de telefone inválido.");
    return;
  }

  const pessoa = {
    nome: document.getElementById('nome').value,
    cpf: limparFormatacao(document.getElementById('cpf').value),
    telefone: limparFormatacao(document.getElementById('telefone').value),
    email: document.getElementById('email').value,
    endereco: document.getElementById('endereco').value,
    nascimento: document.getElementById('nascimento').value,
    sexo: document.getElementById('genero').value,
    observacao: document.getElementById('observacao').value,
    tipo: document.getElementById('tipo').value,
    ministerio: document.getElementById('tipo').value === 'SERVO' ? document.getElementById('ministerio').value : null
  };

  const encontroId = new URLSearchParams(window.location.search).get('encontro');

  let responsavel = null;
  if (calcularIdade(pessoa.nascimento) < 18) {
    responsavel = {
      nome: document.getElementById('responsavelNome').value,
      cpf: limparFormatacao(document.getElementById('responsavelCpf').value),
      telefone: document.getElementById('responsavelTelefone').value
    };
  }

  const arquivoAutorizacao = document.getElementById('autorizacaoArquivo').files[0];
  const formData = new FormData();
  formData.append('inscricao', JSON.stringify({
    pessoa: pessoa,
    encontroId: parseInt(encontroId),
    responsavel: responsavel,
  }));
  formData.append('autorizacao', arquivoAutorizacao);

  inscrever(formData);
});

function mostrarErroModal(mensagem) {
  const modalErro = new bootstrap.Modal(document.getElementById('modalErro'));
  document.getElementById('modalErroMensagem').textContent = mensagem;
  modalErro.show();
}

function mostrarToastSucesso(mensagem) {
  const toastEl = document.getElementById('toastSucesso');
  toastEl.querySelector('.toast-body').textContent = mensagem;
  const toast = new bootstrap.Toast(toastEl);
  toast.show();
  setTimeout(() => {
    window.location.href = 'encontros.html';
  }, 1500);
}

function calcularIdade(dataNascimento) {
  const hoje = new Date();
  const nascimento = new Date(dataNascimento);
  let idade = hoje.getFullYear() - nascimento.getFullYear();
  const mes = hoje.getMonth() - nascimento.getMonth();
  if (mes < 0 || (mes === 0 && hoje.getDate() < nascimento.getDate())) {
    idade--;
  }
  return idade;
}

function acionarCamposResponsavel() {
  const dataNascimento = document.getElementById('nascimento').value;
  if (!dataNascimento) {
    document.getElementById('campos-responsavel').classList.add('d-none');
    document.getElementById('campo-autorizacao').classList.add('d-none');
    return;
  }

  const idade = calcularIdade(dataNascimento);

  if (idade < 18) {
    document.getElementById('campos-responsavel').classList.remove('d-none');
    document.getElementById('campo-autorizacao').classList.remove('d-none');

    document.getElementById('responsavelNome').required = true;
    document.getElementById('responsavelTelefone').required = true;
  } else {
    document.getElementById('campos-responsavel').classList.add('d-none');
    document.getElementById('campo-autorizacao').classList.add('d-none');

    document.getElementById('responsavelNome').required = false;
    document.getElementById('responsavelTelefone').required = false;
  }
}

document.getElementById('nascimento').addEventListener('change', acionarCamposResponsavel);

function formatarCpf(valor) {
  valor = valor.replace(/\D/g, '');
  valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
  valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
  valor = valor.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
  return valor;
}

document.querySelectorAll('.cpf').forEach(input => {
  input.addEventListener('input', function (e) {
    e.target.value = formatarCpf(e.target.value);
  });
});

document.querySelectorAll('input[type="tel"], .telefone').forEach(input => {
  input.addEventListener('input', function (e) {
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
});

function limparFormatacao(valor) {
  return valor.replace(/\D/g, '');
}

function telefoneValido(telefone) {
  const numeros = telefone.replace(/\D/g, '');
  return numeros.length === 10 || numeros.length === 11;
}

document.addEventListener("DOMContentLoaded", function () {
  const hoje = new Date().toISOString().split("T")[0];
  document.getElementById("modalNascimento").setAttribute("max", hoje);
  document.getElementById("nascimento").setAttribute("max", hoje);

  const popoverTriggerList = document.querySelectorAll('[data-bs-toggle="popover"]');
  popoverTriggerList.forEach(pop => new bootstrap.Popover(pop));
});





