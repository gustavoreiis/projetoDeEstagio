package estagio.estagio.dto;

import estagio.estagio.entity.Pessoa;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GrupoExibirDto {
    private Long id;
    private String nome;
    private String cor;
    private String tituloEncontro;
    private List<LiderDto> lideres;
    private int quantidadeParticipantes;

    @Getter
    @Setter
    public static class LiderDto {
        private Long id;
        private String nome;
    }
}
