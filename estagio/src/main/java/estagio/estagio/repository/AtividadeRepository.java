package estagio.estagio.repository;

import estagio.estagio.entity.Atividade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.net.ContentHandler;
import java.time.LocalDate;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
    @Query("""
                SELECT a FROM Atividade a
                WHERE (:descricao IS NULL OR LOWER(a.descricao) LIKE :descricao)
                  AND (:grupo IS NULL OR a.grupoDePessoas = :grupo)
                  AND a.ativa = COALESCE(:status, a.ativa)
                  AND (a.dataAtividade >= COALESCE(:dataInicio, a.dataAtividade))
                  AND (a.dataAtividade <= COALESCE(:dataFim, a.dataAtividade))
                ORDER BY a.descricao ASC
            """)
    Page<Atividade> buscarAtividades(
            @Param("descricao") String descricao,
            @Param("grupo") Atividade.GrupoDePessoas grupo,
            @Param("status") Boolean status,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable
    );
}
