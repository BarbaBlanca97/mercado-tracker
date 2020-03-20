package com.barbablanca.mercadotracker.mailing;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PendingVerificationsRepository extends CrudRepository<PendingVerificationEntity, Integer> {
    public Optional<PendingVerificationEntity> findByToken(String token);
}
