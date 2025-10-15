package estagio.estagio.repository;

import estagio.estagio.entity.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PresencaRepository extends JpaRepository<Presenca, Long> {
    Optional<Presenca> findByPessoaIdAndAtividadeId(Long idPessoa, Long idAtividade);
}
