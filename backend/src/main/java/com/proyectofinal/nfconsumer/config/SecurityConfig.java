package com.proyectofinal.nfconsumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//Avisa a spring de que esto es una configuración y habilita el web security
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    // Identificamos con un Bean para que spring pueda usar esta clase.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        //TO DO
        // Ahora mismo acepta todas las peticiones sin autenticación a los endpoints que coincidan con '/**'
        // Además, deshabilitamos por ahora el cross site request forgery
        http.authorizeHttpRequests(request ->
            request
            .requestMatchers("/users/**")
            .permitAll()
            .requestMatchers("/pagos/**")
            .authenticated()
        ).csrf(csrf -> csrf.disable());
        return http.build();
    }


    //Bean para el usuario con vista.
    @Bean
    public UserDetailsService testUser(PasswordEncoder passwordEncoder){
        User.UserBuilder user = User.builder();
        UserDetails developer = user.username("developer")
            .password(passwordEncoder.encode("1234"))
            .roles()
            .build();
        return new InMemoryUserDetailsManager(developer);
    }

    //Encoder para el password del usuario con vista.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
