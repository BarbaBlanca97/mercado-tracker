package com.barbablanca.mercadotracker.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;

public class SinginException extends Exception {
    public SinginException(String message) {
        super(message);
    }
}
