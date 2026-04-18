package jala.University.Tarefas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jala.University.Tarefas.Infra.security.TokenService;
import jala.University.Tarefas.dto.LoginRequestDTO;
import jala.University.Tarefas.dto.ResgisterRequestDTO;
import jala.University.Tarefas.dto.ResponseDTO;
import jala.University.Tarefas.exception.BusinessException;
import jala.University.Tarefas.model.Factory;
import jala.University.Tarefas.model.User;
import jala.University.Tarefas.repository.RepositoryUser;
import jala.University.Tarefas.service.CurrentUserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RepositoryUser repositoryUser;
    private final TokenService tokenService;
    private final CurrentUserService currentUserService;

    @PostMapping("/login")
public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO body) {

    User user = repositoryUser.login(body.email(), body.password())
            .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

    String token = tokenService.generateToken(user);

    return ResponseEntity.ok(buildResponse(user, token));
}

    @PostMapping("/register")
public ResponseEntity<ResponseDTO> register(@RequestBody ResgisterRequestDTO body) {
    Optional<User> user = repositoryUser.findByEmail(body.email());
    if (user.isPresent()) {
        throw new BusinessException("E-mail já cadastrado.");
    }

    User newUser = Factory.FactoryUser();
    newUser.setName(body.name());
    newUser.setEmail(body.email());


    newUser.setPassword(body.password());

    newUser.setProvider("LOCAL");
    newUser.setRoles(List.of("ROLE_USER"));

    repositoryUser.save(newUser);

    return ResponseEntity.ok(
        buildResponse(newUser, tokenService.generateToken(newUser))
    );
}
    @GetMapping("/me")
    public ResponseEntity<ResponseDTO> getMe() {
        User user = currentUserService.getCurrentUser();
        return ResponseEntity.ok(buildResponse(user, null));
    }

    private ResponseDTO buildResponse(User user, String token) {
        String role = user.getRoles().stream()
                .anyMatch("ROLE_ADMIN"::equals) ? "ADMIN" : "USER";
        return new ResponseDTO(user.getName(), user.getEmail(), token, role);
    }
}
