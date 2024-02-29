package com.austin.flashcard.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Description:
 * @Author: Austin
 * @Create: 2/29/2024 12:24 AM
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                    .httpBasic(Customizer.withDefaults())
                    .formLogin(v -> v.defaultSuccessUrl("/home"));

            return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            UserDetails userDetails = User.builder()
                    .username("admin")
                    .password("{bcrypt}$2a$10$8L6Krvh0jNlcnvJgQfgFGeWKtufKjW6PfuM2LsHTgYCLh9teYTwpu")
                    .roles("ADMIN")
                    .build();

            return new InMemoryUserDetailsManager(userDetails);
        }


}
