package jala.University.Tarefas.repository;

import jala.University.Tarefas.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryCategoria extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByTipoCategoriaIgnoreCase(String tipoCategoria);
}
