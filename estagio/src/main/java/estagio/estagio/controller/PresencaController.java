package estagio.estagio.controller;

import estagio.estagio.Service.PresencaService;
import estagio.estagio.dto.PresencaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/presencas")
public class PresencaController {

    @Autowired
    private PresencaService presencaService;

    @PostMapping
    public ResponseEntity<String> salvarPresencas(@RequestBody List<PresencaDto> presencasDto) {
        presencaService.salvarPresencas(presencasDto);
        return ResponseEntity.ok("Presencas cadastrada com sucesso!");
    }
}
