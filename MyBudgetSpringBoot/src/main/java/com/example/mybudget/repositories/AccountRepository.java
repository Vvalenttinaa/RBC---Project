package com.example.mybudget.repositories;

import com.example.mybudget.models.entities.AccountEntity;
import com.example.mybudget.models.responses.AccountInfo;
import com.example.mybudget.models.dtos.Currency;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    @Modifying
    @Query("UPDATE AccountEntity e SET e.deleted = 1")
    void deleteAll();

    List<AccountEntity>findAllByDeleted(Byte value, Pageable pageable);
    @Query("SELECT e.name FROM AccountEntity e")
    List<String>findAllNames();

    @Query("SELECT new com.example.mybudget.models.responses.AccountInfo (e.id, e.name, e.currency) FROM AccountEntity e")
    List<AccountInfo>findAllAccountInfo();

    @Query("SELECT new com.example.mybudget.models.dtos.Currency (e.currency, e.balance) FROM AccountEntity e")
    List<Currency>findAllBalances();


}
