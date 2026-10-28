package dental.flow.api.boot.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/auth/me").authenticated()
                        .requestMatchers(HttpMethod.POST, "/user").hasRole("DENTISTA")
                        .requestMatchers("/dashboard/**").hasRole("DENTISTA")
                        .requestMatchers("/daily-map/**", "/inventory/**").hasRole("DENTISTA")
                        .requestMatchers(HttpMethod.POST, "/clinic", "/dentist", "/procedure").hasRole("DENTISTA")
                        .requestMatchers(HttpMethod.PUT, "/clinic/**", "/dentist/**", "/procedure/**").hasRole("DENTISTA")
                        .requestMatchers("/patient/**", "/appointment/**").hasAnyRole("DENTISTA", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.GET, "/clinic/**", "/dentist/**", "/procedure/**")
                        .hasAnyRole("DENTISTA", "RECEPCIONISTA")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"code\":\"AUTH001\",\"status\":401,\"message\":\"Autenticação obrigatória.\"}");
                        })
                        .accessDeniedHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"code\":\"AUTH002\",\"status\":403,\"message\":\"Acesso não permitido para este perfil.\"}");
                        })
                )
                .build();
    }
}
