package jala.University.Tarefas.service;

import jala.University.Tarefas.exception.ResourceNotFoundException;
import jala.University.Tarefas.model.User;
import jala.University.Tarefas.repository.RepositoryUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrentUserService {

    private final RepositoryUser repositoryUser;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("Usuario autenticado nao encontrado");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return repositoryUser.findById(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado nao encontrado"));
        }

        String email = authentication.getName();
        if (email == null || email.isBlank() || "anonymousUser".equalsIgnoreCase(email)) {
            throw new ResourceNotFoundException("Usuario autenticado nao encontrado");
        }

        return repositoryUser.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado nao encontrado"));
    }
}
