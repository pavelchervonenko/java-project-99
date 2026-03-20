package hexlet.code.demo.config;

import hexlet.code.demo.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    private final JwtDecoder jwtDecoder;

    private final CustomUserDetailService userDetailService;

    @Bean
    @SuppressWarnings("java:S4502") // CSRF disabled intentionally: stateless JWT-based API, no session cookies
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(
                                "/api/login",
                                "/welcome",
                                "/",
                                "/index.html",
                                "/assets/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer((rs) ->
                        rs.jwt((jwt) -> jwt.decoder(jwtDecoder)))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);

        return builder.build();
    }
}
