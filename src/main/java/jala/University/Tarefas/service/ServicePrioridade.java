package jala.University.Tarefas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jala.University.Tarefas.exception.BusinessException;
import jala.University.Tarefas.exception.ResourceNotFoundException;
import jala.University.Tarefas.model.Prioridade;
import jala.University.Tarefas.repository.RepositoryPrioridade;
import jala.University.Tarefas.repository.RepositoryTarefas;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ServicePrioridade {

    private final RepositoryPrioridade repositoryPrioridade;
    private final RepositoryTarefas repositoryTarefas;

    public Prioridade savePrioridade(Prioridade prioridade) {
        if (prioridade.getNivel() == null || prioridade.getNivel().isEmpty()) {
            throw new BusinessException("Campo Prioridade deve estar preenchido");
        }
        return repositoryPrioridade.save(prioridade);
    }

    // Método para listar todas as prioridades
    public List<Prioridade> findAll() {
        return repositoryPrioridade.findAll();
    }

    public Prioridade findById(long idPrioridade) {
        return repositoryPrioridade.findById(idPrioridade)
                .orElseThrow(() -> new ResourceNotFoundException("Prioridade nao encontrada"));
    }

    public Prioridade resolvePrioridade(Prioridade prioridade) {
        if (prioridade == null) {
            throw new BusinessException("Prioridade deve ser informada.");
        }
        if (prioridade.getId() != null && prioridade.getId() > 0) {
            return findById(prioridade.getId());
        }
        if (prioridade.getNivel() != null && !prioridade.getNivel().isBlank()) {
            return repositoryPrioridade.findByNivelIgnoreCase(prioridade.getNivel().trim())
                    .orElseThrow(() -> new ResourceNotFoundException("Prioridade nao encontrada"));
        }
        throw new BusinessException("Prioridade deve ser informada.");
    }

    public Prioridade updatePrioridade(long idPrioridade, Prioridade novaPrioridade) {
        Prioridade prioridadeAtualizada = findById(idPrioridade);
        prioridadeAtualizada.setNivel(novaPrioridade.getNivel());
        return repositoryPrioridade.save(prioridadeAtualizada);
    }

    public void deletePrioridade(long idPrioridade) {
        Prioridade prioridadeDeletada = findById(idPrioridade);
        if (repositoryTarefas.countByPrioridadeId(prioridadeDeletada.getId()) > 0) {
            throw new BusinessException("Nao e possivel excluir uma prioridade vinculada a tarefas.");
        }
        repositoryPrioridade.deleteById(prioridadeDeletada.getId());
    }
}
