package com.austin.flashcard.auth.config;

import com.austin.flashcard.auth.repository.UserRepository;
import com.austin.flashcard.auth.service.MyUserDetailService;
import com.austin.flashcard.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 * @Description:
 * @Author: Austin
 * @Create: 2/29/2024 12:24 AM
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final static String SALT = "haha";

        @Autowired
        private UserRepository userRepository;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authorize ->
                            authorize.requestMatchers("/test").anonymous()
                                    .requestMatchers("/signIn").anonymous()
                                    .requestMatchers("/signUp").anonymous()
                                    .requestMatchers("/signUpProcess").anonymous()
                                    .requestMatchers("/css/**").permitAll()
                                    .requestMatchers("/images/**").permitAll()
                                    .requestMatchers("/webjars/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .formLogin(v ->
                        v.defaultSuccessUrl("/home")
                        .loginProcessingUrl("/login")
                                .successHandler(new SimpleUrlAuthenticationSuccessHandler("/home"))
                        .loginPage("/signIn")
                                .usernameParameter("email")
                                .passwordParameter("password")
                    ).logout(v -> v.logoutUrl("/logout")
                            .logoutSuccessUrl("/signIn")
                            .permitAll());

            return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return new MyUserDetailService(userRepository);
        }

        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }
}
