package com.barbablanca.mercadotracker.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ExceptionResponse {
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;

    public ExceptionResponse () {}

    public ExceptionResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
