package com.barbablanca.mercadotracker.exceptions;

public class BadPasswordResetAttempt extends Exception {
    public BadPasswordResetAttempt(String message) {
        super(message);
    }
}
