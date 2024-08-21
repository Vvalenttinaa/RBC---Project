package com.example.mybudget.services.impl;

import com.example.mybudget.exception.InsufficientAmountException;
import com.example.mybudget.exception.NotFoundException;
import com.example.mybudget.models.SearchCriteria;
import com.example.mybudget.models.dtos.Currency;
import com.example.mybudget.models.dtos.Transaction;
import com.example.mybudget.models.entities.AccountEntity;
import com.example.mybudget.models.entities.TransactionEntity;
import com.example.mybudget.models.requests.AccountRequest;
import com.example.mybudget.models.requests.FilterRequest;
import com.example.mybudget.models.requests.TransactionRequest;
import com.example.mybudget.repositories.AccountRepository;
import com.example.mybudget.repositories.TransactionRepository;
import com.example.mybudget.services.AccountService;
import com.example.mybudget.services.CurrencyService;
import com.example.mybudget.services.TransactionService;
import com.example.mybudget.specifications.TransactionSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper mapper;
    private final AccountService accountService;
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Transaction> getAll(Pageable pageable) {
        return transactionRepository.findAllByDeleted((byte)0,pageable).stream().map(t-> mapper.map(t, Transaction.class)).collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getAllByAccountId(UUID accountId, Pageable pageable) {
        return transactionRepository.findAllByAccountId(accountId.toString(),pageable).stream().map(t-> mapper.map(t, Transaction.class)).collect(Collectors.toList());
    }


    @Override
    public List<Transaction> getFilteredTransactions(String accountName, Pageable pageable) {
        SearchCriteria accountNameCriteria = new SearchCriteria("accountName", ":", accountName);
        SearchCriteria deletedCriteria = new SearchCriteria("deleted", ":", 0);
        TransactionSpecification accountNameSpec = new TransactionSpecification(accountNameCriteria);
        TransactionSpecification deletedSpec = new TransactionSpecification(deletedCriteria);
        Specification<TransactionEntity> combinedSpec = where(accountNameSpec).and(deletedSpec);
        return transactionRepository.findAll(combinedSpec, pageable)
                .stream()
                .map(t -> mapper.map(t, Transaction.class))
                .collect(Collectors.toList());
    }

    @Override
    public Transaction create(TransactionRequest transactionRequest) {
        AccountEntity accountEntity = accountRepository.findById(UUID.fromString(transactionRequest.getAccountId())).orElseThrow(NotFoundException::new);
        //Transaction on account
        //Increase or enough money
        //Compare in the same valute
        if(transactionRequest.getAmount().compareTo(BigDecimal.ZERO) > 0 ||
                accountEntity.getBalance().compareTo(transactionRequest.getAmount()) > 0){
                accountService.setBalance(transactionRequest.getAmount(), UUID.fromString(transactionRequest.getAccountId()));
            } else {
                throw new InsufficientAmountException();
            }

        TransactionEntity transactionEntity = mapper.map(transactionRequest, TransactionEntity.class);
        transactionEntity.setId(null);
        transactionEntity.setDeleted((byte)0);
        transactionEntity.setAccountId(UUID.fromString(transactionRequest.getAccountId()));
        transactionEntity = transactionRepository.saveAndFlush(transactionEntity);
        entityManager.refresh(transactionEntity);
        return  mapper.map(transactionEntity, Transaction.class);
    }
}
