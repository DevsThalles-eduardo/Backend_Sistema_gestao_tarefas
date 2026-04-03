package jala.University.Tarefas.controller;

import jala.University.Tarefas.dto.CategoriaDTO;
import jala.University.Tarefas.model.Categoria;
import jala.University.Tarefas.service.ServiceCategoria;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/Categoria")
@CrossOrigin("*")
@AllArgsConstructor
public class ControllerCategoria {

    private final ServiceCategoria serviceCategoria;

    @PostMapping
    public Categoria saveCategoria(@RequestBody CategoriaDTO tipocategoria) {
        Categoria categoria = new Categoria();
        categoria.setTipoCategoria(tipocategoria.tipoCategoria());
        return serviceCategoria.saveCategoria(categoria);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> findAll() {
        return ResponseEntity.ok(serviceCategoria.findAll());
    }

    @GetMapping("/{id}")
    public Categoria findById(@PathVariable(name = "id") long idCategoria) {
        return serviceCategoria.findById(idCategoria);
    }

    @PutMapping("/{id}")
    public Categoria updateCategoria(@PathVariable(name = "id") long idCategoria, @RequestBody Categoria categoria) {
        return serviceCategoria.updateCategoria(idCategoria, categoria);
    }

    @DeleteMapping("/{id}")
    public void deleteCategoria(@PathVariable(name = "id") long idCategoria) {
        serviceCategoria.deletecategoria(idCategoria);
    }
}
