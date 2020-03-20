package com.barbablanca.mercadotracker.security;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PasswordResetRepository extends CrudRepository<PasswordReset, Integer> {
    public Optional<PasswordReset> findByCodeHash(String codeHash);
}
