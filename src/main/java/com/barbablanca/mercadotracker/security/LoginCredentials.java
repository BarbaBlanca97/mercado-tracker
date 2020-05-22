package com.barbablanca.mercadotracker.security;

import com.google.common.hash.Hashing;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

public class LoginCredentials {
    @NotNull(message = "Debe ingresar un nombre de usuario o un correo")
    @NotBlank(message = "Debe ingresar un nombre de usuario o un correo")
    public String username;

    @NotNull(message = "Debe ingresar una contraseña")
    @NotBlank(message = "Debe ingresar una contraseña")
    public String password;

    public String getPassword() {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }
}
