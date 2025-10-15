package estagio.estagio.dto;

import lombok.Getter;

@Getter
public class PresencaDto {
    private Long idAtividade;
    private Long idPessoa;
    private boolean presente;
    private String observacao;
}
