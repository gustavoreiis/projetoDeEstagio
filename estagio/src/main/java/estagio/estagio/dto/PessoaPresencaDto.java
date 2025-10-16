package estagio.estagio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PessoaPresencaDto {
    private Long idPessoa;
    private String nome;
    private boolean presente;
    private String observacao;
}
