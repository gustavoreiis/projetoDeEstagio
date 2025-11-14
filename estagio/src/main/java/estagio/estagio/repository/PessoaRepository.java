package estagio.estagio.repository;

import estagio.estagio.dto.ParticipanteTabelaDto;
import estagio.estagio.entity.Pessoa;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    List<Pessoa> findByAtivo(boolean ativo);

    UserDetails findByCpf(String cpf);

    Optional<Pessoa> findPessoaByCpf(String cpf);

    List<Pessoa> findByTipo(Pessoa.TipoPessoa tipo);

    List<Pessoa> findByMinisterio(Pessoa.Ministerio ministerio);

    List<Pessoa> findByCoordenadorIsNotNull();


    boolean existsByCpf(@Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos") String cpf);

    @Query("SELECT p FROM Pessoa p " +
            "WHERE p.tipo = :tipo " +
            "AND p.ativo = true " +
            "AND p.id NOT IN (" +
            "SELECT pg.pessoa.id FROM PessoaGrupo pg " +
            "WHERE pg.grupo.encontro.id = :idEncontro AND pg.lider = true" +
            ")")
    List<Pessoa> findLideresDisponiveis(@Param("idEncontro") Long idEncontro, @Param("tipo") Pessoa.TipoPessoa tipo);

    @Query("""
                SELECT p FROM Pessoa p
                WHERE p.ativo = true
                  AND (:nome IS NULL OR LOWER(p.nome) LIKE :nome)
                  AND (:cpf IS NULL OR p.cpf LIKE :cpf)
                  AND (:tipo IS NULL OR p.tipo = :tipo)
                  AND (:ministerio IS NULL OR p.ministerio = :ministerio)
            """)
    Page<Pessoa> buscarPessoas(
            @Param("nome") String nome,
            @Param("cpf") String cpf,
            @Param("tipo") Pessoa.TipoPessoa tipo,
            @Param("ministerio") Pessoa.Ministerio ministerio,
            Pageable pageable
    );
}