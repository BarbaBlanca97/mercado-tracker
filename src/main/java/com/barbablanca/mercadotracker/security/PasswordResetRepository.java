package com.barbablanca.mercadotracker.security;

import com.barbablanca.mercadotracker.users.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PasswordResetRepository extends CrudRepository<PasswordReset, Integer> {
    Optional<PasswordReset> findByCodeHash(String codeHash);

    void deleteByUserId(int user);
}
