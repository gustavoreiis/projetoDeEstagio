package estagio.estagio.controller;

import estagio.estagio.Service.PessoaService;
import estagio.estagio.entity.Pessoa;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<Pessoa> createPessoa(@RequestBody @Valid Pessoa pessoa) {
        var novaPessoa = pessoaService.criarPessoa(pessoa);
        return ResponseEntity.created(URI.create(novaPessoa.getId().toString())).body(novaPessoa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> getPessoaById(@PathVariable Long id) {
        return pessoaService.buscarPessoaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Pessoa> getPessoaByCpf(@PathVariable String cpf) {
        return pessoaService.buscarPessoaPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> getPessoas() {
        return ResponseEntity.ok(pessoaService.buscarPessoas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePessoaById(@PathVariable Long id, @RequestBody Pessoa pessoa) {
        Pessoa atualizada = pessoaService.atualizarPessoa(id, pessoa);
        if (atualizada != null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePessoaById(@PathVariable Long id) {
        pessoaService.excluirPessoa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verificar-cpf")
    public ResponseEntity<Pessoa> verificarCpf(@RequestParam String cpf) {
        return pessoaService.findByCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lideres/{idEncontro}")
    public ResponseEntity<List<Pessoa>> buscarServos(@PathVariable Long idEncontro) {
        return ResponseEntity.ok(pessoaService.buscarLideres(idEncontro, Pessoa.TipoPessoa.SERVO));
    }
}
