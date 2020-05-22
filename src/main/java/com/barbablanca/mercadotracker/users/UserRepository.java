package com.barbablanca.mercadotracker.users;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    public Optional<UserEntity> findByNameOrEmail(String name, String email);

    public UserEntity findByEmail(String email);
}