package com.furkangunduz.task_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import com.furkangunduz.task_manager.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    

    @Autowired
private CustomUserDetailsService userDetailsService;

@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

   @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  http
    .userDetailsService(userDetailsService)
    .csrf(csrf -> csrf.ignoringRequestMatchers("/logout"))  // BUNU EKLE
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()
        .anyRequest().authenticated()
    )
    .formLogin(form -> form
        .loginPage("/login")
        .defaultSuccessUrl("/tasks", true)
        .permitAll()
    )
    .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .permitAll()
    );
    
    return http.build();
}
}