package jala.University.Tarefas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jala.University.Tarefas.model.Prioridade;

public interface RepositoryPrioridade extends JpaRepository<Prioridade, Long> {

    Optional<Prioridade> findByNivelIgnoreCase(String nivel);
}
