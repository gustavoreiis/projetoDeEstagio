package estagio.estagio.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import estagio.estagio.Service.InscricaoService;
import estagio.estagio.dto.*;
import estagio.estagio.entity.Inscricao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> inscreverPessoa(
            @RequestPart("inscricao") String inscricaoJson,  // Recebe dados JSON como String
            @RequestPart(value = "autorizacao", required = false) MultipartFile autorizacao) {  // Recebe o arquivo

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

            InscricaoRequest inscricaoRequest = objectMapper.readValue(inscricaoJson, InscricaoRequest.class);

            inscricaoRequest.setAutorizacao(autorizacao);

            Inscricao inscricao = inscricaoService.inscreverParticipante(inscricaoRequest);

            return ResponseEntity.ok(inscricao);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
        }
    }

    @GetMapping("/encontro/{idEncontro}")
    public ResponseEntity<List<InscricaoTabelaDto>> listarInscricoesEncontro(
            @PathVariable Long idEncontro,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String grupo,
            @RequestParam(required = false) String pagamento) {
        List<InscricaoTabelaDto> inscricoes = inscricaoService.listarInscricoesTabela(idEncontro, nome, grupo, pagamento);
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

    @GetMapping("/email/{idInscricao}")
    public ResponseEntity<Void> enviarInformacoesPagamento(@PathVariable Long idInscricao) {
        inscricaoService.reenviarInformacoesPagamento(idInscricao);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{idInscricao}")
    public ResponseEntity<?> atualizarInscricao(@PathVariable Long idInscricao, @RequestBody StatusInscricaoDto request) {
        inscricaoService.atualizarInscricao(idInscricao, request);
        return ResponseEntity.ok().build();
    }
}
