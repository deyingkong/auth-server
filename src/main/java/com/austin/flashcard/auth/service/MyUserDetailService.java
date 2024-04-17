package com.austin.flashcard.auth.service;

import com.austin.flashcard.auth.entity.User;
import com.austin.flashcard.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Objects;

/**
 * @Description:
 * @Author: Austin
 * @Create: 2/29/2024 9:46 PM
 */
@Slf4j
public class MyUserDetailService implements UserDetailsManager, UserDetailsPasswordService {

    private UserRepository userRepository;

    public MyUserDetailService(UserRepository repository){
        this.userRepository = repository;
    }

    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        User user = userRepository.findByEmail(username);
        return Objects.isNull(user) ? false : true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(Objects.isNull(user)){
            log.warn("user {} cannot be found in db", username);
            return null;
        }
        boolean disabled = user.getStatus().intValue() < 1;
        return org.springframework.security.core.userdetails.User.builder().username(user.getEmail())
                .password(user.getPassword())
                .accountExpired(disabled)
                .disabled(disabled)
                .build();
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }
}
