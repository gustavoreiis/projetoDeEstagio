package estagio.estagio.dto;

import estagio.estagio.entity.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DetalhesPessoaDto {
    private Long idPessoa;
    private String email;
    private String endereco;
    private Pessoa.TipoPessoa tipo;
    private String ministerio;
    private Pessoa.Sexo sexo;
    private Pessoa.StatusCoordenador statusCoordenador;
    private String nomeResponsavel;
    private String telefoneResponsavel;
    private String cpfResponsavel;
    private String observacao;
}
