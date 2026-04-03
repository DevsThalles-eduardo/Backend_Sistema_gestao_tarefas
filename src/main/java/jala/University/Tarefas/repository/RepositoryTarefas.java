package jala.University.Tarefas.repository;

import jala.University.Tarefas.model.StatusTarefa;
import jala.University.Tarefas.model.Tarefa;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryTarefas extends JpaRepository<Tarefa, Long> {


    List<Tarefa> findByUserId(String userId, Sort sort);

    Optional<Tarefa> findByIdAndUserId(long id, String userId);

    List<Tarefa> findByCategoriaIdAndUserId(long idCategoria, String userId);

    List<Tarefa> findByStatusAndUserId(StatusTarefa status, String userId);

    long countByCategoriaId(long idCategoria);

    long countByPrioridadeId(Long idPrioridade);
}
