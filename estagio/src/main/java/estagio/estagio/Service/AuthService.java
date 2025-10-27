package estagio.estagio.Service;

import estagio.estagio.dto.LoginRequest;
import estagio.estagio.dto.SenhaDto;
import estagio.estagio.entity.Pessoa;
import estagio.estagio.repository.InscricaoRepository;
import estagio.estagio.repository.PessoaRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    private final PessoaRepository pessoaRepository;
    private final PessoaService pessoaService;
    @Autowired
    private InscricaoRepository inscricaoRepository;

    public AuthService(PessoaRepository pessoaRepository, PessoaService pessoaService) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaService = pessoaService;
    }

    public ResponseEntity<?> login(LoginRequest request) {

        Optional<Pessoa> pessoaOpt = pessoaRepository.findPessoaByCpf(request.getCpf());

        if (pessoaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("CPF não encontrado");
        }

        Pessoa pessoa = pessoaOpt.get();

        boolean senhaConfere = false;
        if (pessoa.getSenha() != null && request.getSenha() != null) {
            senhaConfere = BCrypt.checkpw(request.getSenha(), pessoa.getSenha());
        }

        if (pessoa.getSenha() == null || !senhaConfere) {
            System.out.println("Senha inválida para CPF: " + request.getCpf());
            return ResponseEntity.badRequest().body("Senha inválida");
        }

        return ResponseEntity.ok(pessoa);
    }

    public Pessoa cadastrarUsuario(Pessoa pessoa) {
        pessoa.setTipo(Pessoa.TipoPessoa.SERVO);
        pessoa.setCoordenador(Pessoa.StatusCoordenador.PENDENTE);
        return pessoaService.criarPessoa(pessoa);
    }

    public Pessoa cadastrarSenha(SenhaDto senhaDto) {
        System.out.println(senhaDto.getCpf());
        Pessoa pessoa = pessoaRepository.findPessoaByCpf(senhaDto.getCpf())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada para o CPF informado."));

        pessoa.setCoordenador(Pessoa.StatusCoordenador.PENDENTE);
        pessoa.setSenha(senhaDto.getSenha());

        return pessoaService.atualizarPessoa(pessoa.getId(), pessoa);
    }

    public Map<String, Object> verificarCpf(String cpf, LocalDate nascimento, Long encontroId) {
        Map<String, Object> response = new HashMap<>();

        if (cpf == null || !isCpfValido(cpf)) {
            System.out.println(cpf);
            response.put("erro", "Formato de CPF inválido.");
            return response;
        }

        Optional<Pessoa> pessoaOpt = pessoaRepository.findPessoaByCpf(cpf);

        if (pessoaOpt.isPresent()) {
            Pessoa pessoa = pessoaOpt.get();

            if (pessoa.getNascimento().equals(nascimento)) {
                response.put("existe", true);
                response.put("nome", pessoa.getNome());
                response.put("temLogin", pessoa.getSenha() != null && !pessoa.getSenha().isEmpty());

                if (encontroId != null) {
                    boolean jaInscrito = inscricaoRepository.existsByPessoaIdAndEncontroId(pessoa.getId(), encontroId);
                    if (jaInscrito) {
                        response.put("erro", "O CPF " + pessoa.getCpf() + " já está inscrito nesse encontro.");
                    }
                }
            } else {
                response.put("erro", "CPF e/ou data de nascimento incorretos.");
            }
        } else {
            response.put("existe", false);
        }

        return response;
    }

    public boolean isCpfValido(String cpf) {
        if (cpf == null) return false;

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) return false;

        if (cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int soma = 0, resto;

            for (int i = 1; i <= 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i - 1)) * (11 - i);
            }
            resto = (soma * 10) % 11;
            if (resto == 10 || resto == 11) resto = 0;
            if (resto != Character.getNumericValue(cpf.charAt(9))) return false;


            soma = 0;
            for (int i = 1; i <= 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i - 1)) * (12 - i);
            }
            resto = (soma * 10) % 11;
            if (resto == 10 || resto == 11) resto = 0;
            if (resto != Character.getNumericValue(cpf.charAt(10))) return false;

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        return pessoaRepository.findByCpf(cpf);
    }
}
