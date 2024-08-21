package com.example.mybudget.repositories;

import com.example.mybudget.models.entities.TransactionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID>, JpaSpecificationExecutor<TransactionEntity> {
    List<TransactionEntity> findAllByAccountId(String accountId, Pageable pageable);
    List<TransactionEntity>findAllByDeleted(Byte deleted, Pageable pageable);
    @Modifying
    @Query("DELETE FROM TransactionEntity t WHERE t.accountId = :accountId")
    void deleteByAccountId(@Param("accountId") UUID accountId);
    @Modifying
    @Query("UPDATE TransactionEntity e SET e.deleted = 1")
    void deleteAll();
}
