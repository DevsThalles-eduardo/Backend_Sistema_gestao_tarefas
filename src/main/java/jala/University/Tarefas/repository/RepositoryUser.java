package jala.University.Tarefas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jala.University.Tarefas.model.User;

public interface RepositoryUser extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);


    @Query(value = """
    SELECT * FROM users
    WHERE email = :email
    AND password = crypt(:password, password)
    """, nativeQuery = true)
    Optional<User> login(@Param("email") String email, @Param("password") String password);

}
