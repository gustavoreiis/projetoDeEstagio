package estagio.estagio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipanteDto {
    private Long id;
    private String nome;

    public ParticipanteDto(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
