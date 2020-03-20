package com.barbablanca.mercadotracker.exceptions;

public class IdNotFoundException extends Exception {
    public IdNotFoundException() {
        super("No se ha podido recuperar el identificador del producto de la url");
    }
}
