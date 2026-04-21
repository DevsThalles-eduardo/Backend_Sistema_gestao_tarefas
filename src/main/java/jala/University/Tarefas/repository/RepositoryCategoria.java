package jala.University.Tarefas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jala.University.Tarefas.model.Categoria;

public interface RepositoryCategoria extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByTipoCategoriaIgnoreCase(String tipoCategoria);
}
