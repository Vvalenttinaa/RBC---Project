package com.example.mybudget.services;

import com.example.mybudget.models.dtos.Account;
import com.example.mybudget.models.dtos.Currency;
import com.example.mybudget.models.requests.AccountRequest;
import com.example.mybudget.models.responses.AccountInfo;
import jakarta.xml.bind.JAXBException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<Account>getAll(Pageable pageable);
    Account insert(AccountRequest accountRequest);
    void deleteAll();
    Currency getTotalBalance();
//    List<Account> getAllActive(Pageable pageable);
//    public List<Account> getAllDeleted(Pageable pageable);
    List<AccountInfo>getAllAccountInfo();
    List<String> getAllNames();
    String getCurrency(String id);
    void readOldData() throws JAXBException, IOException;
    Account setBalance(BigDecimal balance, UUID id);
}
