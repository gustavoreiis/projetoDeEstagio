package estagio.estagio.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Entity
@Table(name = "encontros")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Encontro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_encontro")
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "date_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    @Column(nullable = false)
    private String local;

    @Column(nullable = false)
    private float preco;

    private String capa;

    @Column(nullable = false)
    private StatusEncontro status;

    public enum StatusEncontro {
        PROGRAMADO,
            ABERTO,
        ENCERRADO,
        CANCELADO
    }
}
