package estagio.estagio.repository;

import estagio.estagio.entity.Encontro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncontroRepository extends JpaRepository<Encontro, Long> {
}
