package com.barbablanca.mercadotracker.mailing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MailVerification {
    @JsonProperty
    private String token;

    MailVerification() {}

    MailVerification(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
