package estagio.estagio.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "pessoas")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pessoa")
    private Long id;

    @Column(nullable = false)
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

    public enum Ministerio {
        ARTES,
        COMUNICACAO_SOCIAL,
        INTERCESSAO,
        MUSICA,
        PREGACAO,
        PROMOCAO_HUMANA
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
