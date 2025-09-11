package estagio.estagio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "responsaveis")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Responsavel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_responsavel")
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String telefone;
}
