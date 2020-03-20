package com.barbablanca.mercadotracker.security;

public class PrincipalCredentials {
    Integer id;
    String name;

    PrincipalCredentials(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
