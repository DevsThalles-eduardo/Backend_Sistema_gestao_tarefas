package jala.University.Tarefas.repository;

import jala.University.Tarefas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryUser extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
}
