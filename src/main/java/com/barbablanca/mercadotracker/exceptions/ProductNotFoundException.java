package com.barbablanca.mercadotracker.exceptions;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String id) {
        super("No se encontró el producto con id "+ id +". Lamentablemente existen productos para los cuales no podemos obtener el id de la url para consultar a la base de datos");
    }
}
