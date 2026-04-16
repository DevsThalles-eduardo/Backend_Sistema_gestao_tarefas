package jala.University.Tarefas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jala.University.Tarefas.model.StatusTarefa;
import jala.University.Tarefas.model.Tarefa;
import jala.University.Tarefas.service.ServiceTarefas;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/Tarefa")
@CrossOrigin("*")
@AllArgsConstructor

public class ControllerTarefas {

    private final ServiceTarefas serviceTarefas;


    @PostMapping
    public Tarefa create(@RequestBody Tarefa tarefa) {
        return serviceTarefas.create(tarefa);
    }

    @GetMapping
    public List<Tarefa> list() {
        return serviceTarefas.FindAll();
    }

    @GetMapping("/Categoria/{id}")
    public List<Tarefa> findByIdCategoria(@PathVariable(name = "id") long idCategoria) {
        return serviceTarefas.findByIdCategoria(idCategoria);
    }

    @GetMapping("/Status/{status}")
    public List<Tarefa> findByStatus(@PathVariable("status") StatusTarefa statusTarefa) {
        return serviceTarefas.findByStatus(statusTarefa);
    }

    @PutMapping("/{id}")
    public Tarefa update(@PathVariable(name = "id") long idTarefa, @RequestBody Tarefa tarefa) {
        return serviceTarefas.update(idTarefa, tarefa);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") long id) {
        serviceTarefas.delete(id);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable("id") Long id, @RequestParam String status) {
        serviceTarefas.atualizarStatusComProcedure(id, status);
        return ResponseEntity.noContent().build();
    }
}

