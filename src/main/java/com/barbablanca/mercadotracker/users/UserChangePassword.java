package com.barbablanca.mercadotracker.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.hash.Hashing;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

public class UserChangePassword {
    @NotNull
    @JsonProperty
    private String oldPassword;
    @JsonProperty
    @NotNull
    private String newPassword;

    UserChangePassword() {}

    UserChangePassword(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public Boolean isValid() {
        return  newPassword != null && !newPassword.equals("");
    }

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
