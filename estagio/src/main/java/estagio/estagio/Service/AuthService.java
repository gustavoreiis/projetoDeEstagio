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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private InscricaoRepository inscricaoRepository;
    @Autowired
    private CpfService cpfService;

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
            return ResponseEntity.badRequest().body("Senha inválida");
        }

        return ResponseEntity.ok(pessoa);
    }

    public Pessoa cadastrarUsuario(Pessoa pessoa) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(pessoa.getSenha());

        pessoa.setSenha(encryptedPassword);
        pessoa.setTipo(Pessoa.TipoPessoa.SERVO);
        pessoa.setCoordenador(Pessoa.StatusCoordenador.PENDENTE);
        pessoa.setAtivo(true);
        return pessoaService.criarPessoa(pessoa);
    }

    public Pessoa cadastrarSenha(SenhaDto senhaDto) {
        Pessoa pessoa = pessoaRepository.findPessoaByCpf(senhaDto.getCpf())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada para o CPF informado."));

        pessoa.setCoordenador(Pessoa.StatusCoordenador.PENDENTE);
        pessoa.setSenha(senhaDto.getSenha());

        return pessoaService.atualizarPessoa(pessoa.getId(), pessoa);
    }

    public Map<String, Object> verificarCpf(String cpf, LocalDate nascimento, Long encontroId) {
        Map<String, Object> response = new HashMap<>();

        if (cpf == null || !cpfService.isCpfValido(cpf)) {
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

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        return pessoaRepository.findByCpf(cpf);
    }
}
