package com.austin.flashcard.auth.config;

import com.austin.flashcard.auth.entity.User;
import com.austin.flashcard.auth.repository.UserRepository;
import com.austin.flashcard.auth.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Description:
 * @Author: Austin
 * @Create: 3/12/2024 11:18 PM
 */
@Slf4j
public final class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private UserRepository repository;

    private PasswordEncoder passwordEncoder;

    private final AuthenticationSuccessHandler delegate = new SavedRequestAwareAuthenticationSuccessHandler();

    Oauth2LoginSuccessHandler(UserRepository u, PasswordEncoder p){
        this.repository = u;
        this.passwordEncoder = p;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("oauth2 login success");

        var email = UserService.retrieveEmailFromAuthentication(authentication);
        if(!isUserExist(email)){
            var username = UserService.retrieveUsernameFromAuthentication(authentication);
            registerUser(email, username, Double.valueOf(Math.random()*100000).toString());
            log.info("a new user is registered:{}, {}", email, username);
        }

        this.delegate.onAuthenticationSuccess(request, response, authentication);
    }

    private boolean isUserExist(String email){
        User user = repository.findByEmail(email);
        return !Objects.isNull(user);
    }

    private void registerUser(String email, String username, String password){
        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .status(Byte.valueOf("1"))
                .createdAt(LocalDateTime.now())
                .editedAt(LocalDateTime.now()).build();
        repository.save(user);
    }


}
