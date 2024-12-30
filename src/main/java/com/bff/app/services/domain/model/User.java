package com.bff.app.services.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String email;
    private String password;
    private String username;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.username = UUID.randomUUID().toString();
    }
}
