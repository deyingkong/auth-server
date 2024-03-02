package com.austin.flashcard.auth.repository;

import com.austin.flashcard.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description:
 * @Author: Austin
 * @Create: 2/29/2024 9:09 PM
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String name);

    User findByEmail(String email);

}
