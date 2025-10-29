package estagio.estagio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class HistoricoAtividadeDto {
    private String descricao;
    private LocalDate data;
    private boolean presente;
}
