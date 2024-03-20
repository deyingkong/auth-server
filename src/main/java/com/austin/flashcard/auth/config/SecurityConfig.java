package com.austin.flashcard.auth.config;

import com.austin.flashcard.auth.filter.PrintRequestFilter;
import com.austin.flashcard.auth.repository.UserRepository;
import com.austin.flashcard.auth.service.MyUserDetailService;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

/**
 * @Description:
 * @Author: Austin
 * @Create: 2/29/2024 12:24 AM
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JdbcOperations jdbcOperations;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults())
                .authorizationService(new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository))
                ;    // Enable OpenID Connect 1.0
        http
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/signIn"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                // Accept access tokens for User Info and/or Client Registration
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults()));

        return http.build();
    }

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
                                v.loginProcessingUrl("/login")
                                .loginPage("/signIn")
                                .usernameParameter("email")
                                .passwordParameter("password")
                ).logout(v -> v.logoutUrl("/logout")
                        .logoutSuccessUrl("/signIn")
                        .permitAll())
                .oauth2Client(oauth2 -> oauth2
                        .clientRegistrationRepository(clientRegistrationRepository)
                ).oauth2Login(login ->
                        login.clientRegistrationRepository(clientRegistrationRepository)
                                .loginPage("/signIn")
                                .successHandler(authenticationSuccessHandler())
                )
                .addFilterBefore(new PrintRequestFilter(), LogoutFilter.class);

        return http.build();
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new Oauth2LoginSuccessHandler(userRepository, passwordEncoder());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
