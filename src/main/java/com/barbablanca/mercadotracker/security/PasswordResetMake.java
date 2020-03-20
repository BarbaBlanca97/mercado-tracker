package com.barbablanca.mercadotracker.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class PasswordResetMake {
    @JsonProperty
    private Integer code;
    @JsonProperty
    private String newPassword;

    private String error;

    PasswordResetMake () {}

    public Integer getCode() {
        return code;
    }

    public String getNewPassword() {
        return Hashing.sha256()
                .hashString(newPassword, StandardCharsets.UTF_8)
                .toString();
    }

    public Boolean isValid() {
        if (code == null || code.equals("")) {
            error = "Debe ingresar el codigo"; return false;
        }

        if (newPassword == null || newPassword.equals("")) {
            error = "Debe ingresar la nueva contraseña"; return false;
        }

        return true;
    }

    public String getError() {
        return error;
    }
}
