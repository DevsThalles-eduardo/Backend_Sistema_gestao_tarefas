package jala.University.Tarefas.Infra.security;

import jala.University.Tarefas.model.Prioridade;
import jala.University.Tarefas.model.User;
import jala.University.Tarefas.model.Factory;
import jala.University.Tarefas.repository.RepositoryPrioridade;
import jala.University.Tarefas.repository.RepositoryUser;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RepositoryUser repositoryUser;
    private final PasswordEncoder passwordEncoder;
    private final RepositoryPrioridade repositoryPrioridade;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            if (repositoryUser.findByEmail("admin@admin.com").isEmpty()) {
                User admin = Factory.FactoryUser();
                admin.setName("Administrador Master");
                admin.setEmail("admin@admin.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setProvider("LOCAL");
                admin.setRoles(List.of("ROLE_ADMIN", "ROLE_USER"));
                
                repositoryUser.save(admin);
                System.out.println("✅ Usuário ADMIN criado: admin@admin.com / admin123");
            }
            // Dentro do initDatabase no DataInitializer.java
            if (repositoryPrioridade.findAll().isEmpty()) {
                Prioridade p1 = new Prioridade(); p1.setNivel("ALTA"); repositoryPrioridade.save(p1);
                Prioridade p2 = new Prioridade(); p2.setNivel("MÉDIA"); repositoryPrioridade.save(p2);
                Prioridade p3 = new Prioridade(); p3.setNivel("BAIXA"); repositoryPrioridade.save(p3);
                System.out.println("✅ Prioridades iniciais criadas!");
            }
        };
    }
}