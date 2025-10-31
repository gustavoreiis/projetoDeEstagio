package estagio.estagio.controller;

import estagio.estagio.Service.PessoaService;
import estagio.estagio.dto.DetalhesPessoaDto;
import estagio.estagio.dto.ParticipanteTabelaDto;
import estagio.estagio.dto.PessoaStatusDto;
import estagio.estagio.dto.SolicitacaoAtualizacaoCoordenadorDto;
import estagio.estagio.entity.Pessoa;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<Pessoa> criarPessoa(@RequestBody @Valid Pessoa pessoa) {
        var novaPessoa = pessoaService.criarPessoa(pessoa);
        return ResponseEntity.created(URI.create(novaPessoa.getId().toString())).body(novaPessoa);
    }

    @GetMapping("/{idPessoa}")
    public ResponseEntity<Pessoa> buscarPessoaPorId(@PathVariable Long idPessoa) {
        return pessoaService.buscarPessoaPorId(idPessoa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/detalhes/{idPessoa}")
    public ResponseEntity<DetalhesPessoaDto> buscarDetalhesPessoa(@PathVariable Long idPessoa) {
        DetalhesPessoaDto detalhes = pessoaService.buscarDetalhesPessoa(idPessoa);
        return ResponseEntity.ok(detalhes);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Pessoa> buscarPessoaPorCpf(@PathVariable String cpf) {
        return pessoaService.buscarPessoaPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ParticipanteTabelaDto>> listarPessoas() {
        List<ParticipanteTabelaDto> pessoas =  pessoaService.listarPessoas();
        return ResponseEntity.ok(pessoas);
    }

    @GetMapping("historico/{idPessoa}")
    public ResponseEntity<?> buscarHistoricoPessoa(@PathVariable Long idPessoa) {
        Map<String, Object> historico = pessoaService.buscarHistoricoPessoa(idPessoa);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/solicitacoes")
    public ResponseEntity<?> buscarSolicitacoesCoordenadores() {
        Map<String, Object> solicitacoes = pessoaService.buscarSolicitacoesCoordenadores();
        return ResponseEntity.ok(solicitacoes);
    }

    @PutMapping("/{idPessoa}")
    public ResponseEntity<Void> atualizarPessoa(@PathVariable Long idPessoa, @RequestBody Pessoa pessoa) {
        Pessoa atualizada = pessoaService.atualizarPessoa(idPessoa, pessoa);
        if (atualizada != null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/solicitacoes")
    public ResponseEntity<Void> alterarStatusCoordenador(@RequestBody List<SolicitacaoAtualizacaoCoordenadorDto> solicitacoes) {
        pessoaService.alterarStatusCoordenador(solicitacoes);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable Long id) {
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

    @GetMapping("/inativos")
    public ResponseEntity<List<PessoaStatusDto>> buscarInativos() {
        List<PessoaStatusDto> inativos = pessoaService.buscarInativos();
        return ResponseEntity.ok(inativos);
    }
}
