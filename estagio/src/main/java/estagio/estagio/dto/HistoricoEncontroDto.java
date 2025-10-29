package estagio.estagio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class HistoricoEncontroDto {
    private String titulo;
    private LocalDateTime data;
}
