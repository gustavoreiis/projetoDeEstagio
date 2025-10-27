package estagio.estagio.dto;

import estagio.estagio.entity.Pessoa;
import estagio.estagio.entity.Responsavel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Getter
@Setter
public class InscricaoRequest {
    private Pessoa pessoa;
    private Long encontroId;
    private Responsavel responsavel;
    private MultipartFile autorizacao;
}
