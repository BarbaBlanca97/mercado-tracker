package com.barbablanca.mercadotracker.security;

import com.barbablanca.mercadotracker.users.UserEntity;

import javax.persistence.*;
import java.util.Random;

@Entity
public class PasswordReset {
    @Id
    @GeneratedValue
    private Integer id;
    @OneToOne
    private UserEntity user;
    private String codeHash;

    PasswordReset () {}

    PasswordReset (UserEntity user) {
        this.user = user;
    }

    public Integer generateCode() {
        Random random = new Random();

        return (user.getId() * 100000) + random.nextInt(99999);
    }

    public String getCodeHash() {
        return codeHash;
    }

    public void setCodeHash(String codeHash) {
        this.codeHash = codeHash;
    }

    public UserEntity getUser() {
        return user;
    }
}
