package estagio.estagio.controller;

import estagio.estagio.Service.PessoaGrupoService;
import estagio.estagio.dto.ParticipanteDto;
import estagio.estagio.entity.PessoaGrupo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/participantes-grupos")
public class PessoaGrupoController {

    private final PessoaGrupoService pessoaGrupoService;

    public PessoaGrupoController(PessoaGrupoService pessoaGrupoService) {
        this.pessoaGrupoService = pessoaGrupoService;
    }

    @GetMapping("/{idGrupo}")
    public ResponseEntity<List<ParticipanteDto>> listarParticipantesDoGrupo(@PathVariable Long idGrupo) {
        List<ParticipanteDto> listaParticipantes = pessoaGrupoService.listarParticipantesDoGrupo(idGrupo);
        return ResponseEntity.ok(listaParticipantes);
    }

    @GetMapping("encontro/{idEncontro}")
    public ResponseEntity<List<ParticipanteDto>> listarParticipantesSemGrupo(@PathVariable Long idEncontro) {
        List<ParticipanteDto> listaParticipantes = pessoaGrupoService.listarParticipantesSemGrupo(idEncontro);
        return ResponseEntity.ok(listaParticipantes);
    }

    @PostMapping("/{idParticipante}/{idGrupo}")
    public ResponseEntity<PessoaGrupo> adicionarParticipante(@PathVariable Long idParticipante, @PathVariable Long idGrupo) {
        PessoaGrupo pessoaGrupo = pessoaGrupoService.adicionarPessoa(idParticipante, idGrupo);
        return ResponseEntity.ok(pessoaGrupo);
    }

    @DeleteMapping("/{idParticipante}/{idGrupo}")
    public ResponseEntity<Void> removerParticipante(@PathVariable Long idParticipante, @PathVariable Long idGrupo) {
        pessoaGrupoService.removerPessoa(idParticipante, idGrupo);
        return ResponseEntity.noContent().build();
    }

}
