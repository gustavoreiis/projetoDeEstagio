package estagio.estagio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResumoInscricoesEncontro {
    private int total;
    private int pagos;
    private int naoPagos;
    private int servos;
    private int participantes;
}
