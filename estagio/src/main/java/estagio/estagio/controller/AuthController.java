package estagio.estagio.controller;

import estagio.estagio.Service.AuthService;
import estagio.estagio.Service.PessoaService;
import estagio.estagio.Service.TokenService;
import estagio.estagio.dto.LoginRequest;
import estagio.estagio.dto.LoginResponseDto;
import estagio.estagio.dto.SenhaDto;
import estagio.estagio.entity.Pessoa;
import estagio.estagio.repository.InscricaoRepository;
import estagio.estagio.repository.PessoaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(request.getCpf(), request.getSenha());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var pessoa = (Pessoa) auth.getPrincipal();

            if (pessoa.getCoordenador() != Pessoa.StatusCoordenador.COORDENADOR) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("Acesso negado. Verifique seu acesso com um coordenador.");
            }
            var token = tokenService.generateToken(pessoa);

            return ResponseEntity.ok(new LoginResponseDto(token));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("CPF ou senha incorretos.");
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity cadastrarUsuario(@RequestBody @Valid Pessoa pessoa) {
        if (pessoaRepository.findByCpf(pessoa.getCpf()) != null) return ResponseEntity.badRequest().build();

        authService.cadastrarUsuario(pessoa);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/verificar-cpf")
    public ResponseEntity<?> verificarCpf(
            @RequestParam String cpf,
            @RequestParam LocalDate nascimento,
            @RequestParam(required = false) Long encontroId) {

        Map<String, Object> response = authService.verificarCpf(cpf, nascimento, encontroId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cadastrar-senha")
    public ResponseEntity<Pessoa> cadastrarSenha(@RequestBody SenhaDto senhaDto) {
        Pessoa novaPessoa = authService.cadastrarSenha(senhaDto);
        return ResponseEntity.ok(novaPessoa);
    }
}
