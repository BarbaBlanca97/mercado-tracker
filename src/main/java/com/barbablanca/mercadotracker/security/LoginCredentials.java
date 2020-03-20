package com.barbablanca.mercadotracker.security;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class LoginCredentials {
    public String username;
    public String password;

    public String getPassword() {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }
}
