package estagio.estagio.controller;

import estagio.estagio.Service.AtividadeService;
import estagio.estagio.dto.AtividadeDto;
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
}
