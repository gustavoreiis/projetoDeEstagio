package estagio.estagio.repository;

import estagio.estagio.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findByEncontroId(Long encontroId);

}
