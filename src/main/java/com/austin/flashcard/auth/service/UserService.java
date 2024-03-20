package com.austin.flashcard.auth.service;

import com.austin.flashcard.auth.entity.User;
import com.austin.flashcard.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @Description:
 * @Author: Austin
 * @Create: 2/29/2024 9:13 PM
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isUserExist(String email){
        User user = userRepository.findByEmail(email);
        return !Objects.isNull(user);
    }

    public Optional<User> findUserByEmail(String email){
        return Optional.of(userRepository.findByEmail(email));
    }

    public void registerUser(String email, String username, String password){
        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .status(Byte.valueOf("1"))
                .createdAt(LocalDateTime.now())
                .editedAt(LocalDateTime.now()).build();
        userRepository.save(user);
    }

    public static String retrieveEmailFromAuthentication(Authentication authentication){
        //SecurityContext context = SecurityContextHolder.getContext();
        //Authentication authentication = context.getAuthentication();
        if(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User)
                    authentication.getPrincipal();
            return securityUser.getUsername();
        }
        if(authentication instanceof OAuth2AuthenticationToken){
            OAuth2User oAuth2User = (DefaultOidcUser)authentication.getPrincipal();
            return oAuth2User.getAttribute("email");
        }
        return null;
    }

    public static String retrieveUsernameFromAuthentication(Authentication authentication){
        if(authentication instanceof OAuth2AuthenticationToken){
            OAuth2User oAuth2User = (DefaultOidcUser)authentication.getPrincipal();
            return oAuth2User.getAttribute("name");
        }
        return null;
    }

}
