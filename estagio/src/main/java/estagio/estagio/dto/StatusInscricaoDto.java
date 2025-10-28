package estagio.estagio.dto;

import lombok.Getter;

@Getter
public class StatusInscricaoDto {
    private boolean pago;
    private boolean autorizado;
    private String observacao;
}
