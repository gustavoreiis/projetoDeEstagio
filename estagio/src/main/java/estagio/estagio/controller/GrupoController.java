package estagio.estagio.controller;

import estagio.estagio.Service.GrupoService;
import estagio.estagio.dto.GrupoDto;
import estagio.estagio.dto.GrupoExibirDto;
import estagio.estagio.entity.Grupo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/grupos")
public class GrupoController {

    private final GrupoService grupoService;
    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @PostMapping
    public ResponseEntity<Grupo> criarGrupo(@RequestBody GrupoDto grupoDto) {
        Grupo novoGrupo = grupoService.criarGrupo(grupoDto);
        return ResponseEntity.ok(novoGrupo);
    }

    @GetMapping("/{idEncontro}")
    public ResponseEntity<List<GrupoExibirDto>> listarGrupos(@PathVariable Long idEncontro) {
        return ResponseEntity.ok(grupoService.listarGrupos(idEncontro));
    }

    @DeleteMapping("/{idGrupo}")
    public ResponseEntity<Void> excluirGrupo(@PathVariable Long idGrupo) {
        grupoService.excluirGrupo(idGrupo);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idGrupo}")
    public ResponseEntity<Grupo> atualizarGrupo(@PathVariable Long idGrupo, @RequestBody GrupoDto grupoDto) {
        grupoService.atualizarGrupo(idGrupo, grupoDto);
        return ResponseEntity.noContent().build();
    }
}
