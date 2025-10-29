package estagio.estagio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class ParticipanteTabelaDto {
    private Long idPessoa;
    private String nome;
    private String cpf;
    private String telefone;
    private String grupo;
    private LocalDate dataNascimento;
    private int frequencia;
}
