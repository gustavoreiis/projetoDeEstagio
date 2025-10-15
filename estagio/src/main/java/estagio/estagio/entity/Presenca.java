package estagio.estagio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "presencas")
@NoArgsConstructor
@Getter
@Setter
public class Presenca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_presenca")
    private Long id;

    @Column(nullable = false)
    private boolean presente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_atividade")
    private Atividade atividade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa")
    private Pessoa pessoa;

    private String observacao;
}
