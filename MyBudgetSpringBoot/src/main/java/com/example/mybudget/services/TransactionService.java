package com.example.mybudget.services;


import com.example.mybudget.models.dtos.Transaction;
import com.example.mybudget.models.requests.TransactionRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<Transaction>getAll(Pageable page);
    List<Transaction> getAllByAccountId(UUID accountId, Pageable pageable);
   // Transaction insert(TransactionRequest transactionRequest);
    public List<Transaction> getFilteredTransactions(String accountName, Pageable pageable);
    public Transaction create(TransactionRequest transactionRequest);
}
