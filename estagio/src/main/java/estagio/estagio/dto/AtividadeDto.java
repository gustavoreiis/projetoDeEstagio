package estagio.estagio.dto;

import estagio.estagio.entity.Atividade;
import lombok.Getter;

import java.util.Date;

@Getter
public class AtividadeDto {
    private String descricao;
    private Date dataAtividade;
    private Atividade.GrupoDePessoas grupoDePessoas;
}
