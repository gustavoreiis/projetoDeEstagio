package estagio.estagio.dto;

import estagio.estagio.entity.Pessoa;
import estagio.estagio.entity.Responsavel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class InscricaoRequest {
    public Pessoa pessoa;
    public Long encontroId;
    public Responsavel responsavel;
}
