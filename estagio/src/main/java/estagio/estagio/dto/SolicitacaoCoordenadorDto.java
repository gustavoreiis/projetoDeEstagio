package estagio.estagio.dto;

import estagio.estagio.entity.Pessoa;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Getter
public class SolicitacaoCoordenadorDto {
    private Long idPessoa;
    private String nome;
    private Pessoa.StatusCoordenador statusCoordenador;
}
