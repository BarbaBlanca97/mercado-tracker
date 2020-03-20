package com.barbablanca.mercadotracker.mailing;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Entity
public class PendingVerificationEntity {
    @Id
    Integer userId;
    String token;

    PendingVerificationEntity() {}

    PendingVerificationEntity(Integer userId) {
        Random random = new Random();
        byte[] bytes = new byte[7];
        for (int i = 0; i < 7; i++) {
            bytes[i] = (byte) (97 + random.nextInt(25));
        }

        String token = userId.toString() + new String(bytes, StandardCharsets.UTF_8);

        this.userId = userId;
        this.token = token;
    }

    PendingVerificationEntity(Integer userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Integer getUserId() {
        return userId;
    }
}
