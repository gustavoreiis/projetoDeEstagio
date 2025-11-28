package estagio.estagio.controller;

import estagio.estagio.entity.Encontro;
import estagio.estagio.Service.EncontroService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/encontros")
public class EncontroController {

    private final EncontroService encontroService;

    public EncontroController(EncontroService encontroService) {
        this.encontroService = encontroService;
    }

    @PostMapping
    public ResponseEntity<Encontro> criarEncontro(
            @RequestParam("titulo") String titulo,
            @RequestParam("dataHoraInicio") LocalDateTime dataHoraInicio,
            @RequestParam("dataHoraFim") LocalDateTime dataHoraFim,
            @RequestParam("local") String local,
            @RequestParam("preco") float preco,
            @RequestParam("descricao") String descricao,
            @RequestParam(value = "capa", required = false) MultipartFile capa) {
        Encontro encontro = encontroService.criarEncontro(titulo, dataHoraInicio, dataHoraFim, local, preco, descricao, capa);
        return ResponseEntity.ok(encontro);
    }

    @GetMapping("/{idEncontro}")
    public ResponseEntity<Encontro> buscarEncontroPorId(@PathVariable Long idEncontro) {
        var encontro = encontroService.buscarEncontroPorId(idEncontro);
        return encontro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Encontro>> listarEncontros() {
        var encontros = encontroService.buscarEncontros();
        return ResponseEntity.ok(encontros);
    }

    @PutMapping("/{idEncontro}")
    public ResponseEntity<?> atualizarEncontro(
            @PathVariable Long idEncontro,
            @RequestParam String titulo,
            @RequestParam String descricao,
            @RequestParam LocalDateTime dataHoraInicio,
            @RequestParam LocalDateTime dataHoraFim,
            @RequestParam String local,
            @RequestParam float preco,
            @RequestParam(value = "capa", required = false) MultipartFile capa,
            @RequestParam(required = false) boolean aberto) {

        try {
            Encontro encontroAtualizado = encontroService.atualizarEncontro(
                    idEncontro, titulo, descricao, dataHoraInicio, dataHoraFim, local, preco, capa, aberto);
            return ResponseEntity.ok(encontroAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Encontro não encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar encontro");
        }
    }

    @DeleteMapping("/{idEncontro}")
    public ResponseEntity<Void> deletarEncontro(@PathVariable Long idEncontro) {
        encontroService.deletarEncontro(idEncontro);
        return ResponseEntity.noContent().build();
    }
}
