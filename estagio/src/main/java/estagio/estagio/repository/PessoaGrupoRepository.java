package estagio.estagio.repository;

import estagio.estagio.dto.ParticipanteDto;
import estagio.estagio.entity.PessoaGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PessoaGrupoRepository extends JpaRepository<PessoaGrupo, Long> {
    List<PessoaGrupo> findByGrupoId(Long idGrupo);
    List<PessoaGrupo> findBygrupoIdAndLiderTrue(Long idGrupo);
    boolean existsByPessoaIdAndGrupoEncontroIdAndLiderTrue(Long idPessoa, Long idGrupo);
    List<PessoaGrupo> findByGrupoIdAndLiderFalse(Long idGrupo);
    void deleteByGrupoId(Long idGrupo);
    PessoaGrupo findByPessoaIdAndGrupoId(Long idPessoa, Long idGrupo);

    @Query(value = "SELECT p.id_pessoa AS idPessoa, p.nome AS nome " +
            "FROM pessoas p " +
            "JOIN inscricoes i ON i.id_pessoa = p.id_pessoa " +
            "WHERE i.id_encontro = :idEncontro " +
            "AND p.tipo = 'PARTICIPANTE' " +
            "AND p.id_pessoa NOT IN ( " +
            "   SELECT pg.id_pessoa FROM pessoa_grupo pg " +
            "   JOIN grupos g ON pg.id_grupo = g.id_grupo " +
            "   WHERE g.id_encontro = :idEncontro )",
            nativeQuery = true)
    List<ParticipanteDto> findParticipantesSemGrupo(@Param("idEncontro") Long idEncontro);
}
