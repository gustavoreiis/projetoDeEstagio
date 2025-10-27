package estagio.estagio.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscricoes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscricao")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "id_pessoa")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "id_encontro")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Encontro encontro;

    @Column(nullable = true)
    private boolean autorizado;

    @Column(nullable = true)
    private String arquivoAutorizacao;

    @Column(nullable = true)
    private boolean pago;

    @Column(nullable = false)
    private LocalDate dataInscricao;
}
