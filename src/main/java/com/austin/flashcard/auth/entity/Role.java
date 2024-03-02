package com.austin.flashcard.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: Austin
 * @Create: 2/29/2024 9:07 PM
 */
@Entity
@Data
public class Role {
    @Id
    private Integer id;

    private String roleName;

    private String roleCode;

    private LocalDateTime createdAt;

    private LocalDateTime editedAt;

}
