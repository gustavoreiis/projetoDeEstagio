package estagio.estagio.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GrupoDto {
    private String nome;
    private String cor;
    private Long idEncontro;
    private List<Long> idLideres;
}
