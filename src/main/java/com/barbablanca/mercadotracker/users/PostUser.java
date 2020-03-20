package com.barbablanca.mercadotracker.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PostUser {
    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    private String error = "";

    PostUser () {}

    public Boolean isValid() {
        if (username == null || username.equals("")) {
            error = "Debe proporcionar un nombre de usuario";
            return false;
        }

        if (email == null || email.equals("")) {
            error = "Debe proporcionar correo electrónico";
            return false;
        }

        if (password == null || password.equals("")) {
            error = "Debe proporcionar una contraseña";
            return false;
        }

        return true;
    }

    public String getError() {
        return error;
    }

    public UserEntity asUserEntity() {
        String hashedPassword = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();

        return new UserEntity(username, email, hashedPassword, false);
    }
}
