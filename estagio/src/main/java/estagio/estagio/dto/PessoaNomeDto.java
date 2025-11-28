package estagio.estagio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaNomeDto {
    private Long id;
    private String nome;

    public PessoaNomeDto(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
