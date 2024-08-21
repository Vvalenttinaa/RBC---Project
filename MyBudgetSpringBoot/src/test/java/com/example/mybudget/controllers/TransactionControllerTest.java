package com.example.mybudget.controllers;
import com.example.mybudget.exception.InsufficientAmountException;
import com.example.mybudget.models.dtos.Account;
import com.example.mybudget.models.dtos.Transaction;
import com.example.mybudget.models.requests.TransactionRequest;
import com.example.mybudget.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        int page = 0;
        int size = 5;
        List<Transaction> transactions = List.of(new Transaction(
                UUID.randomUUID().toString(),
                "100.00",
                "Payment 123",
                UUID.randomUUID().toString(),
                "Johnn Doe",
                "USD",
                (byte) 0
        ));

        when(transactionService.getAll(PageRequest.of(page, size))).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = transactionController.getAll(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        assertEquals(transactions.size(), response.getBody().size());
        verify(transactionService, times(1)).getAll(PageRequest.of(page, size));
    }

    @Test
    void getAllByAccountId() {
        int page = 0;
        int size = 5;
        UUID accountId = UUID.randomUUID();
        List<Transaction> transactions = List.of(new Transaction(
                UUID.randomUUID().toString(),
                "100.00",
                "Payment 123",
                accountId.toString(),
                "John Doe",
                "USD",
                (byte) 0
        ), new Transaction(
                UUID.randomUUID().toString(),
                "100.00",
                "Payment 123",
                 UUID.randomUUID().toString(),
                "Johnn Doe",
                "USD",
                (byte) 0
        ));

        when(transactionService.getAllByAccountId(accountId, PageRequest.of(page, size))).thenReturn(List.of(new Transaction(
                UUID.randomUUID().toString(),
                "100.00",
                "Payment 123",
                accountId.toString(),
                "John Doe",
                "USD",
                (byte) 0
        )));

        ResponseEntity<List<Transaction>> response = transactionController.getAllByAccountId(page, size, accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(transactionService, times(1)).getAllByAccountId(accountId, PageRequest.of(page, size));
    }


    @Test
    void create() {
        String accountId = UUID.randomUUID().toString();
        TransactionRequest transactionRequest = new TransactionRequest(new BigDecimal(10), "Transaction", accountId);
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), new BigDecimal(10).toString(), "Transaction", "John", null, "EUR", (byte)0);

        when(transactionService.create(transactionRequest)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.create(transactionRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction.getId(), response.getBody().getId());
        verify(transactionService, times(1)).create(transactionRequest);
    }

    @Test
    void handleInsufficientAmountException() {
        String errorMessage = "Insufficient funds";
        InsufficientAmountException exception = new InsufficientAmountException(errorMessage);

        ResponseEntity<String> response = transactionController.handleInsufficientAmountException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
}
