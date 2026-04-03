package jala.University.Tarefas.repository;

import jala.University.Tarefas.model.Prioridade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryPrioridade extends JpaRepository<Prioridade, Long> {

    Optional<Prioridade> findByNivelIgnoreCase(String nivel);
}
