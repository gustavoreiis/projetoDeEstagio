package estagio.estagio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "atividades")
@NoArgsConstructor
@Getter
@Setter
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atividade")
    private Long id;

    private String descricao;

    @Column(name = "data_atividade")
    private LocalDate dataAtividade;

    @Column(name = "grupo_pessoas")
    @Enumerated(EnumType.STRING)
    private GrupoDePessoas grupoDePessoas;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusAtividade statusAtividade;

    public enum StatusAtividade {
        CONCLUIDO,
        ATIVO
    }

    public enum GrupoDePessoas {
        ARTES,
        COMUNICACAO_SOCIAL,
        INTERCESSAO,
        MUSICA,
        PREGACAO,
        PROMOCAO_HUMANA,
        SERVO,
        PARTICIPANTE
    }
}
