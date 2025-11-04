package estagio.estagio.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "pessoas")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class Pessoa implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pessoa")
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "O campo nome é obrigatório.")
    private String nome;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
    private String cpf;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String endereco;

    @Column(name = "data_nascimento", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate nascimento;

    @Column(nullable = true)
    private String senha;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "responsavel_id")
    private Responsavel responsavel;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoPessoa tipo;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private String observacao;

    @Enumerated(EnumType.STRING)
    private StatusCoordenador coordenador;

    @Enumerated(EnumType.STRING)
    private Ministerio ministerio;

    @Column(nullable = true)
    private boolean ativo;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.coordenador == StatusCoordenador.COORDENADOR) return List.of(new SimpleGrantedAuthority("ROLE_COORDENADOR"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return cpf;
    }

    public enum Ministerio {
        ARTES("Artes"),
        COMUNICACAO_SOCIAL("Comunicação Social"),
        INTERCESSAO("Intercessão"),
        MUSICA("Música"),
        PREGACAO("Pregação"),
        PROMOCAO_HUMANA("Promoção Humana");

        private final String nomeFormatado;

        Ministerio(String nomeFormatado) {
            this.nomeFormatado = nomeFormatado;
        }

        @JsonValue
        public String getNomeFormatado() {
            return nomeFormatado;
        }
    }

    public enum Sexo {
        MASCULINO,
        FEMININO
    }

    public enum TipoPessoa {
        PARTICIPANTE,
        SERVO
    }

    public enum StatusCoordenador {
        COORDENADOR,
        PENDENTE,
        NEGADO
    }
}
