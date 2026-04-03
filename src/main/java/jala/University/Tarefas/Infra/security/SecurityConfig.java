package jala.University.Tarefas.Infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Habilitamos o CORS aqui para ler a configuração que criamos abaixo
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    authorize.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
                    authorize.requestMatchers(HttpMethod.GET, "/auth/me").authenticated();
                    authorize.requestMatchers(HttpMethod.GET, "/Categoria").hasAnyRole("USER", "ADMIN");
                    authorize.requestMatchers(HttpMethod.GET, "/Categoria/**").hasAnyRole("USER", "ADMIN");
                    authorize.requestMatchers(HttpMethod.POST, "/Categoria/**").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.PUT, "/Categoria/**").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.DELETE, "/Categoria/**").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.GET, "/Prioridade").hasAnyRole("USER", "ADMIN");
                    authorize.requestMatchers(HttpMethod.GET, "/Prioridade/**").hasAnyRole("USER", "ADMIN");
                    authorize.requestMatchers(HttpMethod.POST, "/Prioridade/**").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.PUT, "/Prioridade/**").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.DELETE, "/Prioridade/**").hasRole("ADMIN");
                    authorize.requestMatchers("/Tarefa/**").hasAnyRole("USER", "ADMIN");
                    authorize.requestMatchers(
                            "/",
                            "/error",
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/h2-console/**"
                    ).permitAll();
                    authorize.anyRequest().authenticated();
                })
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }

    // 2. Criamos as regras exatas do que o CORS vai permitir
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite o seu frontend acessar (pode colocar "http://localhost:5174" ou "*" para todos no ambiente dev)
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Necessário se for usar cookies ou tokens mais complexos depois

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica essa regra para TODOS os endpoints
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
