package com.barbablanca.mercadotracker.security;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordResetMake {
    @NotNull( message = "Debe proporcionar un codigo")
    @JsonProperty
    private Integer code;
    @NotNull( message = "Debe proporcionar una contraseña")
    @NotBlank( message = "Debe proporcionar una contraseña")
    @Size(min = 6, message = "La contraseña debe tener 6 caracteres como mínimo")
    @JsonProperty
    private String newPassword;

    PasswordResetMake () {}

    public Integer getCode() {
        return code;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public Boolean isValid() {
        return true;
    }

    public String getError() {
        return "";
    }
}
