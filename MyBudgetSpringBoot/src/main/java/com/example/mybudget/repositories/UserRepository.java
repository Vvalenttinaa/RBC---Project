package com.example.mybudget.repositories;

import com.example.mybudget.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("SELECT e FROM UserEntity e")
    Optional<UserEntity> findUser();
    @Query("SELECT e.currentCurrency FROM UserEntity e")
    Optional<String>findCurrency();
    @Query("SELECT e.updated FROM UserEntity e")
    Optional<Byte>findUpdated();
}
