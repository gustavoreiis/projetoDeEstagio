package estagio.estagio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String cpf;
    private String senha;
}
