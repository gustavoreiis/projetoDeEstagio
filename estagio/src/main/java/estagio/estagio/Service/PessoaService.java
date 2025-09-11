package estagio.estagio.Service;

import estagio.estagio.entity.Pessoa;
import estagio.estagio.repository.PessoaRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public Pessoa criarPessoa(Pessoa pessoa) {
        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new RuntimeException("O CPF informado já está cadastrado.");
        }

        if (pessoa.getSenha() != null && !pessoa.getSenha().isEmpty()) {
            String hashed = BCrypt.hashpw(pessoa.getSenha(), BCrypt.gensalt());
            pessoa.setSenha(hashed);
        }

        return pessoaRepository.save(pessoa);
    }

    public Optional<Pessoa> buscarPessoaPorId(Long id) {
        return pessoaRepository.findById(id);
    }

    public Optional<Pessoa> buscarPessoaPorCpf(String cpf) {
        return pessoaRepository.findByCpf(cpf);
    }

    public List<Pessoa> buscarPessoas() {
        return pessoaRepository.findAll();
    }

    public Pessoa atualizarPessoa(Long id, Pessoa pessoa) {
        var pessoaExistente = pessoaRepository.findById(id);

        if (pessoaExistente.isEmpty()) return null;

        var atual = pessoaExistente.get();

        if (pessoa.getNome() != null) atual.setNome(pessoa.getNome());
        if (pessoa.getCpf() != null) atual.setCpf(pessoa.getCpf());
        if (pessoa.getTelefone() != null) atual.setTelefone(pessoa.getTelefone());
        if (pessoa.getEmail() != null) atual.setEmail(pessoa.getEmail());
        if (pessoa.getEndereco() != null) atual.setEndereco(pessoa.getEndereco());
        if (pessoa.getNascimento() != null) atual.setNascimento(pessoa.getNascimento());

        if (pessoa.getSenha() != null && !pessoa.getSenha().isEmpty()) {
            if (!pessoa.getSenha().startsWith("$2a$")) {
                String hashed = BCrypt.hashpw(pessoa.getSenha(), BCrypt.gensalt());
                atual.setSenha(hashed);
            } else {
                atual.setSenha(pessoa.getSenha());
            }
        }

        if (pessoa.getResponsavel() != null) atual.setResponsavel(pessoa.getResponsavel());
        if (pessoa.getTipo() != null) atual.setTipo(pessoa.getTipo());
        if (pessoa.getSexo() != null) atual.setSexo(pessoa.getSexo());
        if (pessoa.getMinisterio() != null) atual.setMinisterio(pessoa.getMinisterio());
        if (pessoa.getObservacao() != null) atual.setObservacao(pessoa.getObservacao());
        if (pessoa.getCoordenador() != null) atual.setCoordenador(pessoa.getCoordenador());

        return pessoaRepository.save(atual);
    }


    public void excluirPessoa(Long id) {
        pessoaRepository.findById(id).ifPresent(pessoaRepository::delete);
    }

    public Optional<Pessoa> findByCpf(String cpf) {
        return pessoaRepository.findByCpf(cpf);
    }

    public List<Pessoa> buscarLideres(Long idEncontro, Pessoa.TipoPessoa tipo) {
        return pessoaRepository.findLideresDisponiveis(idEncontro, tipo);
    }
}
