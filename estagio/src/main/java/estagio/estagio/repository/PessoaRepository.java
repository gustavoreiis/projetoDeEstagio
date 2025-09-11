package estagio.estagio.repository;

import estagio.estagio.entity.Pessoa;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Optional<Pessoa> findByCpf(String cpf);
    Optional<Pessoa> findByCpfAndNascimento(String cpf, LocalDate nascimento);
    List<Pessoa> findByTipo(Pessoa.TipoPessoa tipo);


    boolean existsByCpf(@Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos") String cpf);

    @Query("SELECT p FROM Pessoa p " +
            "WHERE p.tipo = :tipo AND p.id NOT IN (" +
            "SELECT pg.pessoa.id FROM PessoaGrupo pg " +
            "WHERE pg.grupo.encontro.id = :idEncontro AND pg.lider = true" +
            ")")
    List<Pessoa> findLideresDisponiveis(@Param("idEncontro") Long idEncontro, @Param("tipo") Pessoa.TipoPessoa tipo);

}
