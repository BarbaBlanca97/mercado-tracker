package com.barbablanca.mercadotracker.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.hash.Hashing;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.nio.charset.StandardCharsets;

public class UserChangePassword {
    @NotNull
    @JsonProperty
    private String oldPassword;

    @NotNull( message = "Debe proporcionar una contraseña")
    @NotBlank( message = "Debe proporcionar una contraseña")
    @Size(min = 6, message = "La contraseña debe tener 6 caracteres como mínimo")
    @JsonProperty
    private String newPassword;

    UserChangePassword() {}

    public String getOldPassword() {
        return Hashing.sha256()
                .hashString(oldPassword, StandardCharsets.UTF_8)
                .toString();
    }

    public String getNewPassword() {
        return  Hashing.sha256()
                .hashString(newPassword, StandardCharsets.UTF_8)
                .toString();
    }
}
