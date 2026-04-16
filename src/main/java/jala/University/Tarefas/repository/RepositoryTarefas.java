package jala.University.Tarefas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jala.University.Tarefas.model.StatusTarefa;
import jala.University.Tarefas.model.Tarefa;

@Repository
public interface RepositoryTarefas extends JpaRepository<Tarefa, Long> {


    List<Tarefa> findByUserId(String userId, Sort sort);

    Optional<Tarefa> findByIdAndUserId(long id, String userId);

    List<Tarefa> findByCategoriaIdAndUserId(long idCategoria, String userId);

    List<Tarefa> findByStatusAndUserId(StatusTarefa status, String userId);

    long countByCategoriaId(long idCategoria);

    long countByPrioridadeId(Long idPrioridade);

    @Modifying
    @Transactional
    @Query(value = "CALL sp_atualizar_status_tarefa(:id, :status)", nativeQuery = true)
    void atualizarStatusComProcedure(@Param("id") Long id, @Param("status") String status);
}
