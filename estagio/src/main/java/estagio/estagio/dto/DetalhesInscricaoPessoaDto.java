package estagio.estagio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DetalhesInscricaoPessoaDto {
    private String email;
    private LocalDate dataInscricao;
    private String endereco;
    private String nomeResponsavel;
    private String telefoneResponsavel;
    private String statusAutorizado;
    private String autorizacao;
    private String observacao;

}
