package estagio.estagio.controller;

import estagio.estagio.Service.InscricaoService;
import estagio.estagio.dto.InscricaoRequest;
import estagio.estagio.entity.Inscricao;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inscricoes")
public class InscricaoController {
    private final InscricaoService inscricaoService;

    public InscricaoController(InscricaoService inscricaoService) {
        this.inscricaoService = inscricaoService;
    }

    @PostMapping
    public ResponseEntity<?> inscreverPessoa(@RequestBody @Valid InscricaoRequest request) {
        try {
            Inscricao inscricao = inscricaoService.inscreverParticipante(request);
            return ResponseEntity.ok(inscricao);
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
        }
    }

    @DeleteMapping("/{idInscricao}")
    public ResponseEntity<Void> cancelarinscricao(@PathVariable Long idInscricao) {
        inscricaoService.cancelarInscricao(idInscricao);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/encontro/{idEncontro}")
    public ResponseEntity<List<Inscricao>> listarPorEncontro(@PathVariable Long idEncontro) {
        List<Inscricao> inscricoes = inscricaoService.buscarInscricoesPorEncontro(idEncontro);
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/participante/{idPessoa}")
    public ResponseEntity<List<Inscricao>> listarPorPessoa(@PathVariable Long idPessoa) {
        List<Inscricao> inscricoes = inscricaoService.buscarInscricoesPorPessoa(idPessoa);
        return ResponseEntity.ok(inscricoes);
    }
}
