package br.org.sesisenai.clinipet.security.config;

import br.org.sesisenai.clinipet.security.filter.AutenticacaoFiltro;
import br.org.sesisenai.clinipet.security.service.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class AutenticacaoConfig {
    @Autowired
    private JpaService jpaService;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jpaService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("/**"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/api/login", "/login").permitAll()

                // AGENDA
                .requestMatchers(HttpMethod.GET, "/agenda").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.GET, "/agenda/*").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.POST, "/agenda").hasAuthority("ATENDENTE")
                .requestMatchers(HttpMethod.PUT, "/agenda/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/agenda/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")

                // ANIMAl
                .requestMatchers(HttpMethod.GET, "/animal").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.GET, "/animal/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.POST, "/animal").hasAuthority("ATENDENTE")
                .requestMatchers(HttpMethod.PUT, "/animal/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/animal/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")

                // ATENDENTE
                .requestMatchers(HttpMethod.GET, "/atendente").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.GET, "/atendente/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.POST, "/atendente").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.PUT, "/atendente/*").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/atendente/*").hasAuthority("VETERINARIO")

                // CLIENTE
                .requestMatchers(HttpMethod.GET, "/cliente").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.GET, "/cliente/*").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.POST, "/cliente").hasAuthority("ATENDENTE")
                .requestMatchers(HttpMethod.PUT, "/cliente/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/cliente/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")

                // PRONTUARIO
                .requestMatchers(HttpMethod.GET, "/prontuario").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.GET, "/prontuario/*").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.POST, "/prontuario").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.PUT, "/prontuario/*").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/prontuario/*").hasAuthority("VETERINARIO")

                // SERVICO ****
                //.requestMatchers(HttpMethod.GET, "").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.GET, "/servico").permitAll()
                .requestMatchers(HttpMethod.GET, "/servico/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/servico").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.PUT, "/servico/*").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/servico/*").hasAuthority("VETERINARIO")

                // VETERINARIO ****
                //.requestMatchers(HttpMethod.GET, "").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.GET, "/veterinario").permitAll()
                .requestMatchers(HttpMethod.GET, "/veterinario/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/veterinario").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.PUT, "/veterinario/*").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/veterinario/*").hasAuthority("VETERINARIO")

                .anyRequest().authenticated();

        http.csrf().disable();
        http.cors().configurationSource(corsConfigurationSource());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticacaoFiltro(jpaService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}