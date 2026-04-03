package jala.University.Tarefas.controller;

import jala.University.Tarefas.dto.PrioridadeDTO;
import jala.University.Tarefas.model.Prioridade;
import jala.University.Tarefas.service.ServicePrioridade;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Prioridade")
@CrossOrigin("*")
@AllArgsConstructor
public class ControllerPrioridade {

    private ServicePrioridade servicePrioridade;

    @PostMapping
    public Prioridade savePrioridade(@RequestBody PrioridadeDTO prioridadeDTO) {
        Prioridade prioridade = new Prioridade();
        prioridade.setNivel(prioridadeDTO.nivelPrioridade());
        return servicePrioridade.savePrioridade(prioridade);
    }

    @GetMapping
    public List<Prioridade> findAll() {
        return servicePrioridade.findAll();
    }

    @GetMapping("/{id}")
    public Prioridade findById(@PathVariable(name = "id") long idPrioridade) {
        return servicePrioridade.findById(idPrioridade);
    }

    @PutMapping("/{id}")
    public Prioridade updatePrioridade(@PathVariable(name = "id") long idcategoria, @RequestBody Prioridade prioridadeAtualizada) {
        return servicePrioridade.updatePrioridade(idcategoria, prioridadeAtualizada);
    }

    @DeleteMapping("/{id}")
    public void deletePrioridade(@PathVariable(name = "id") long idPrioridade) {
        servicePrioridade.deletePrioridade(idPrioridade);
    }
}
