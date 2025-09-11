package estagio.estagio.repository;

import estagio.estagio.entity.Inscricao;
import estagio.estagio.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    List<Inscricao> findByEncontroId(Long encontroId);
    List<Inscricao> findByPessoaId(Long pessoaId);
    boolean existsByPessoaIdAndEncontroId(Long pessoaId, Long encontroId);

    Long pessoa(Pessoa pessoa);
}
