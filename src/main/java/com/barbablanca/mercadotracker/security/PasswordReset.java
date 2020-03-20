package com.barbablanca.mercadotracker.security;

import com.barbablanca.mercadotracker.users.UserEntity;
import com.google.common.hash.Hashing;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
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

        Integer code = (user.getId() * 100000) + random.nextInt(99999);

        this.codeHash = Hashing.sha256()
                .hashString(code.toString(), StandardCharsets.UTF_8)
                .toString();

        return code;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public UserEntity getUser() {
        return user;
    }
}
