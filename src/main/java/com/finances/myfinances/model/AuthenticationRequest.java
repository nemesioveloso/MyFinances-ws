package com.finances.myfinances.model;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String usernameOrEmail;
    private String password;
}
