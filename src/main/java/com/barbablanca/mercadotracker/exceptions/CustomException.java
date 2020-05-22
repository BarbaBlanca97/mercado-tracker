package com.barbablanca.mercadotracker.exceptions;

public class CustomException extends Exception {
    private final int code;

    public CustomException() {
        super("Ha ocurrido un error");
        this.code = 400;
    }

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() { return this.code; }
}
