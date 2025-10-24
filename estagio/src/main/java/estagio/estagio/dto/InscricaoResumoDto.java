package estagio.estagio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class InscricaoResumoDto {
    private Long id;
    private String nome;
    private String telefone;
    private String grupo;
    private String pago;
    private LocalDate dataNascimento;
}
