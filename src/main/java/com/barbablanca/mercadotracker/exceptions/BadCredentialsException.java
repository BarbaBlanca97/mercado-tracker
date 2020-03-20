package com.barbablanca.mercadotracker.exceptions;

public class BadCredentialsException extends Exception {
    public enum Field{ PASSWORD, USERNAME };

    public BadCredentialsException() {
        super("Usuario y/o contraseña incorrectos");
    }

    public BadCredentialsException(Field field) {
        super(field == Field.PASSWORD ? "Contraseña incorrecta" : "Usuario incorrecto");
    }
}