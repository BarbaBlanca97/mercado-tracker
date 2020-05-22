package com.barbablanca.mercadotracker.security;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class LoginCredentials {
    @NotNull(message = "Debe ingresar un nombre de usuario o un correo")
    @NotBlank(message = "Debe ingresar un nombre de usuario o un correo")
    private String username;

    @NotNull(message = "Debe ingresar una contraseña")
    @NotBlank(message = "Debe ingresar una contraseña")
    private String password;
}
