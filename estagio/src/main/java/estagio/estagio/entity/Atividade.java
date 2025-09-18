package estagio.estagio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Date dataAtividade;

    @Column(name = "grupo_pessoas")
    private String grupoDePessoas;
}
