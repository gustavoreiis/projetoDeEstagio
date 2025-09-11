package estagio.estagio.controller;

import estagio.estagio.Service.AuthService;
import estagio.estagio.Service.PessoaService;
import estagio.estagio.dto.LoginRequest;
import estagio.estagio.dto.SenhaDto;
import estagio.estagio.entity.Pessoa;
import estagio.estagio.repository.InscricaoRepository;
import estagio.estagio.repository.PessoaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/verificar-cpf")
    public ResponseEntity<?> verificarCpf(
            @RequestParam String cpf,
            @RequestParam LocalDate nascimento,
            @RequestParam(required = false) Long encontroId) {

        Map<String, Object> response = authService.verificarCpf(cpf, nascimento, encontroId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Pessoa> cadastrarUsuario(@RequestBody @Valid Pessoa pessoa) {
        Pessoa novaPessoa = authService.cadastrarUsuario(pessoa);
        return ResponseEntity.ok(novaPessoa);
    }

    @PostMapping("/cadastrar-senha")
    public ResponseEntity<Pessoa> cadastrarSenha(@RequestBody SenhaDto senhaDto) {
        Pessoa novaPessoa = authService.cadastrarSenha(senhaDto);
        return ResponseEntity.ok(novaPessoa);
    }
}
