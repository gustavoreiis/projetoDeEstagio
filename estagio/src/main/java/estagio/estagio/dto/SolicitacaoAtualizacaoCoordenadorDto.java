package estagio.estagio.dto;

import estagio.estagio.entity.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SolicitacaoAtualizacaoCoordenadorDto {
    private Long idPessoa;
    private Pessoa.StatusCoordenador novoStatus;
}
