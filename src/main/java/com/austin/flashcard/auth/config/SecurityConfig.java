package com.austin.flashcard.auth.config;

import com.austin.flashcard.auth.filter.RequestLoggingFilter;
import com.austin.flashcard.auth.repository.UserRepository;
import com.austin.flashcard.auth.service.MyUserDetailService;
import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.time.Duration;

/**
 * @Description:
 * @Author: Austin
 * @Create: 2/29/2024 12:24 AM
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

/*    @Value("${root.url}")
    private String rootUrl;*/

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
                //.registeredClientRepository(getRegisteredClientRepository())
                .oidc(Customizer.withDefaults())  // Enable OpenID Connect 1.0
                .authorizationService(new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository))
                //.tokenGenerator(new JwtGenerator(encoder))
        ;
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
                        .jwt(Customizer.withDefaults()))
                .addFilterBefore(new RequestLoggingFilter(), HeaderWriterFilter.class)
        ;

        return http.build();
    }

/*    public RegisteredClientRepository getRegisteredClientRepository(){
        var client = registeredClientRepository.findByClientId("flashcard-web");
        RegisteredClient newClient = RegisteredClient.from(client).tokenSettings(
                TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(4))
                        .build()).build();
        log.info("new client:{}", newClient);
        return new InMemoryRegisteredClientRepository(newClient);
    }*/

/*
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(rootUrl)
                .build();
    }

*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/signIn").anonymous()
                                .requestMatchers("/signUp").anonymous()
                                .requestMatchers("/signUpProcess").anonymous()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers("/webjars/**").permitAll()
                                .requestMatchers("/favicon.ico").permitAll()
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
                .addFilterBefore(new RequestLoggingFilter(), LogoutFilter.class)
        ;

        return http.build();
    }

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        ForwardedHeaderFilter filter = new ForwardedHeaderFilter();
        FilterRegistrationBean<ForwardedHeaderFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return registration;
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
