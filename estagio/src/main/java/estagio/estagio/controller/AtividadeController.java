package estagio.estagio.controller;

import estagio.estagio.Service.AtividadeService;
import estagio.estagio.dto.AtividadeDto;
import estagio.estagio.dto.PessoaPresencaDto;
import estagio.estagio.entity.Atividade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {
    private final AtividadeService atividadeService;

    public AtividadeController(AtividadeService atividadeService) {
        this.atividadeService = atividadeService;
    }

    @GetMapping
    public ResponseEntity<List<Atividade>> listarAtividades() {
        List<Atividade> atividades = atividadeService.listarAtividades();
        return ResponseEntity.ok(atividades);
    }

    @PostMapping
    public ResponseEntity<Atividade> criarAtividade(@RequestBody AtividadeDto atividadeDto) {
        Atividade novaAtividade = atividadeService.criarAtividade(atividadeDto);
        return ResponseEntity.ok(novaAtividade);
    }

    @PutMapping("/{idAtividade}")
    public ResponseEntity<Atividade> atualizarAtividade(@PathVariable Long idAtividade, @RequestBody AtividadeDto atividadeDto) {
        Atividade atividadeAtualizada = atividadeService.atualizarAtividade(idAtividade, atividadeDto);
        return ResponseEntity.ok(atividadeAtualizada);
    }

    @DeleteMapping("/{idAtividade}")
    public ResponseEntity<Void> deletarAtividade(@PathVariable Long idAtividade) {
        atividadeService.excluirAtividade(idAtividade);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idAtividade}")
    public ResponseEntity<List<PessoaPresencaDto>> buscarPessoas(@PathVariable Long idAtividade) {
        List<PessoaPresencaDto> pessoas = atividadeService.buscarPessoas(idAtividade);
        return ResponseEntity.ok(pessoas);
    }
}
