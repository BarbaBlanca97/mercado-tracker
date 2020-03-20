package com.barbablanca.mercadotracker.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordResetRequest {
    @JsonProperty("email")
    private String email;

    PasswordResetRequest() {}

    public String getEmail() {
        return email;
    }
}
