package jala.University.Tarefas.service;

import jala.University.Tarefas.exception.ResourceNotFoundException;
import jala.University.Tarefas.model.Categoria;
import jala.University.Tarefas.model.StatusTarefa;
import jala.University.Tarefas.model.Tarefa;
import jala.University.Tarefas.model.User;
import jala.University.Tarefas.repository.RepositoryTarefas;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceTarefas {

    private final RepositoryTarefas repositoryTarefas;
    private final ServiceCategoria serviceCategoria;
    private final ServicePrioridade servicePrioridade;
    private final CurrentUserService currentUserService;

    public Tarefa create(Tarefa tarefa) {
        User currentUser = currentUserService.getCurrentUser();
        tarefa.setCategoria(serviceCategoria.resolveCategoria(tarefa.getCategoria()));
        tarefa.setPrioridade(servicePrioridade.resolvePrioridade(tarefa.getPrioridade()));
        tarefa.setUser(currentUser);
        return repositoryTarefas.save(tarefa);
    }

    public List<Tarefa> FindAll() {
        User currentUser = currentUserService.getCurrentUser();
        Sort sort = Sort.by("id").ascending().and(Sort.by("categoria").descending());
        return repositoryTarefas.findByUserId(currentUser.getId(), sort);
    }

    public Tarefa findById(long idTarefa) {
        User currentUser = currentUserService.getCurrentUser();
        return repositoryTarefas.findByIdAndUserId(idTarefa, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa nao identificada"));
    }

    public Tarefa update(long idTarefa, Tarefa novaTarefa) {
        Tarefa tarefaAtualizada = findById(idTarefa);
        tarefaAtualizada.setCategoria(serviceCategoria.resolveCategoria(novaTarefa.getCategoria()));
        tarefaAtualizada.setPrioridade(servicePrioridade.resolvePrioridade(novaTarefa.getPrioridade()));
        tarefaAtualizada.setTitulo(novaTarefa.getTitulo());
        tarefaAtualizada.setDescricao(novaTarefa.getDescricao());
        tarefaAtualizada.setDataInicio(novaTarefa.getDataInicio());
        tarefaAtualizada.setDataFim(novaTarefa.getDataFim());
        tarefaAtualizada.setStatus(novaTarefa.getStatus());
        tarefaAtualizada.setNotaTarefa(novaTarefa.getNotaTarefa());
        return repositoryTarefas.save(tarefaAtualizada);
    }

    public void delete(long idTarefa) {
        Tarefa tarefaDeletada = findById(idTarefa);
        repositoryTarefas.deleteById(tarefaDeletada.getId());
    }

    public List<Tarefa> findByIdCategoria(long idCategoria) {
        User currentUser = currentUserService.getCurrentUser();
        Categoria categoriateste = serviceCategoria.findById(idCategoria);
        return repositoryTarefas.findByCategoriaIdAndUserId(categoriateste.getId(), currentUser.getId());
    }

    public List<Tarefa> findByStatus(StatusTarefa statusTarefa) {
        User currentUser = currentUserService.getCurrentUser();
        StatusTarefa statusTarefaTeste = StatusTarefa.valueOf(statusTarefa.getDescricao().toUpperCase());
        return repositoryTarefas.findByStatusAndUserId(statusTarefaTeste, currentUser.getId());
    }
}
