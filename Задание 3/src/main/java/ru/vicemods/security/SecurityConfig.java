package ru.vicemods.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth", "/register", "/css/**", "/js/**", "/images/**", "/error").permitAll()
                        .anyRequest().authenticated())
                .formLogin(login -> login.loginPage("/auth").loginProcessingUrl("/login")
                        .usernameParameter("email").defaultSuccessUrl("/?loginSuccess", true).failureUrl("/auth?error"))
                .logout(logout -> logout.logoutSuccessUrl("/?logout").invalidateHttpSession(true).deleteCookies("JSESSIONID"));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
