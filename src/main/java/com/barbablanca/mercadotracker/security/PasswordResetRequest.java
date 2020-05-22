package com.barbablanca.mercadotracker.security;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PasswordResetRequest {
    @NotNull(message = "Debe proporcionar un correo")
    @NotBlank(message = "Debe proporcionar un correo")
    @Email(message = "El formato de correo no es válido")
    @JsonProperty("email")
    private String email;

    PasswordResetRequest() {}

    public String getEmail() {
        return email;
    }
}
