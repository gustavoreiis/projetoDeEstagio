package estagio.estagio.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import estagio.estagio.dto.AtividadeDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    private boolean ativa;

    public Atividade(AtividadeDto atividadeDto) {
        this.descricao = atividadeDto.getDescricao();
        this.dataAtividade = atividadeDto.getDataAtividade();
        this.grupoDePessoas = atividadeDto.getGrupoDePessoas();
        this.ativa = true;
    }

    public enum GrupoDePessoas {
        ARTES("Artes"),
        COMUNICACAO_SOCIAL("Comunicação Social"),
        INTERCESSAO("Intercessão"),
        MUSICA("Música"),
        PREGACAO("Pregação"),
        PROMOCAO_HUMANA("Promoção Humana"),
        SERVO("Servos"),
        PARTICIPANTE("Participantes");

        private final String nomeFormatado;

        GrupoDePessoas(String nomeFormatado) {
            this.nomeFormatado = nomeFormatado;
        }

        @JsonValue
        public String getNomeFormatado() {
            return nomeFormatado;
        }
    }
}
