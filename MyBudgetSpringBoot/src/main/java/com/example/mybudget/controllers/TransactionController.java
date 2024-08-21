package com.example.mybudget.controllers;

import com.example.mybudget.exception.InsufficientAmountException;
import com.example.mybudget.models.dtos.Transaction;
import com.example.mybudget.models.requests.TransactionRequest;
import com.example.mybudget.services.TransactionService;
import com.example.mybudget.services.impl.CurrencyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping()
    ResponseEntity<List<Transaction>> getAll(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int size){
        return ResponseEntity.ok(transactionService.getAll(PageRequest.of(page,size)));
    }

    @GetMapping("/account/{accountId}")
    ResponseEntity<List<Transaction>> getAllByAccountId(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "5") int size, @PathVariable UUID accountId){
        return ResponseEntity.ok(transactionService.getAllByAccountId(accountId, PageRequest.of(page,size)));
    }

    @GetMapping("/transactionsFiltered")
    public List<Transaction> getTransactions(@RequestParam(required = false) String accountName, @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int size) {
        logger.error(accountName);
        if(accountName.equals("All")){
            logger.error(accountName);
            return this.transactionService.getAll(PageRequest.of(page, size));
        }else
            return transactionService.getFilteredTransactions(accountName, PageRequest.of(page, size));
    }

    @PostMapping()
    ResponseEntity<Transaction>create(@RequestBody TransactionRequest transactionRequest){
        return ResponseEntity.ok(transactionService.create(transactionRequest));
    }

    @ExceptionHandler(InsufficientAmountException.class)
    public ResponseEntity<String> handleInsufficientAmountException(InsufficientAmountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


}
