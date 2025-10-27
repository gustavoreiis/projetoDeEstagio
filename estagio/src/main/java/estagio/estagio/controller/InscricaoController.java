package estagio.estagio.controller;

import estagio.estagio.Service.InscricaoService;
import estagio.estagio.dto.DetalhesInscricaoPessoaDto;
import estagio.estagio.dto.InscricaoRequest;
import estagio.estagio.dto.InscricaoTabelaDto;
import estagio.estagio.dto.ResumoInscricoesEncontro;
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

//    @DeleteMapping("/{idInscricao}")
//    public ResponseEntity<Void> cancelarinscricao(@PathVariable Long idInscricao) {
//        inscricaoService.cancelarInscricao(idInscricao);
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping("/encontro/{idEncontro}")
    public ResponseEntity<List<InscricaoTabelaDto>> listarInscricoesEncontro(@PathVariable Long idEncontro) {
        List<InscricaoTabelaDto> inscricoes = inscricaoService.listarInscricoesTabela(idEncontro);
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/encontro/{idEncontro}/resumo")
    public ResponseEntity<ResumoInscricoesEncontro> obterResumoInscricoesEncontro(@PathVariable Long idEncontro) {
        ResumoInscricoesEncontro resumo = inscricaoService.buscarDadosInscricoesEncontro(idEncontro);
        return ResponseEntity.ok(resumo);
    }

    @GetMapping("/{idInscricao}")
    public ResponseEntity<DetalhesInscricaoPessoaDto> obterDetalhesInscricao(@PathVariable Long idInscricao) {
        DetalhesInscricaoPessoaDto detalhes = inscricaoService.buscarDetalhesInscricao(idInscricao);
        return ResponseEntity.ok(detalhes);
    }

//    @GetMapping("/participante/{idPessoa}")
//    public ResponseEntity<List<Inscricao>> listarPorPessoa(@PathVariable Long idPessoa) {
//        List<Inscricao> inscricoes = inscricaoService.buscarInscricoesPorPessoa(idPessoa);
//        return ResponseEntity.ok(inscricoes);
//    }
}
