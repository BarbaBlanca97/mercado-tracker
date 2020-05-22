package com.barbablanca.mercadotracker.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserChangePassword {
    @NotNull
    @JsonProperty
    private String oldPassword;

    @NotNull( message = "Debe proporcionar una contraseña")
    @NotBlank( message = "Debe proporcionar una contraseña")
    @Size(min = 6, message = "La contraseña debe tener 6 caracteres como mínimo")
    @JsonProperty
    private String newPassword;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    UserChangePassword() {}

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
