package com.example.mybudget.controllers;

import com.example.mybudget.models.dtos.Account;
import com.example.mybudget.models.dtos.Currency;
import com.example.mybudget.models.requests.AccountRequest;
import com.example.mybudget.models.responses.AccountInfo;
import com.example.mybudget.repositories.AccountRepository;
import com.example.mybudget.repositories.UserRepository;
import com.example.mybudget.services.AccountService;
import com.example.mybudget.services.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


import java.math.BigDecimal;
import java.util.*;

public class AccountControllerTest {
    @Mock
    private AccountService accountService;


    @InjectMocks
    private AccountController accountController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getAll() {
        int page = 0;
        int size = 5;
        List<Account> accounts = List.of(new Account(UUID.randomUUID().toString(), "jOHN", "EUR", 100.0, (byte)0 ));

        when(accountService.getAll(PageRequest.of(page, size))).thenReturn(accounts);
        ResponseEntity<List<Account>> response = accountController.getAll(page, size);

        assertEquals(1, response.getBody().size());
        assertEquals("jOHN", response.getBody().get(0).getName());
        assertEquals(100.0, response.getBody().get(0).getBalance());
        assertEquals("EUR", response.getBody().get(0).getCurrency());
        verify(accountService, times(1)).getAll(PageRequest.of(page, size));
    }


    @Test
    void insert() {
        AccountRequest accountRequest = new AccountRequest("John", "EUR", new BigDecimal(89));

        Account expectedAccount = new Account(UUID.randomUUID().toString(), "John", "EUR", 89.0, (byte)0);
        when(accountService.insert(accountRequest)).thenReturn(expectedAccount);

        ResponseEntity<Account> response = accountController.insert(accountRequest);

        assertEquals(expectedAccount, response.getBody());
        assertEquals(expectedAccount.getName(), response.getBody().getName());
        assertEquals(expectedAccount.getBalance(), response.getBody().getBalance());
        verify(accountService, times(1)).insert(accountRequest);
    }

    @Test
    void delete() {
        doNothing().when(accountService).deleteAll();

        ResponseEntity<Void> response = accountController.delete();

        assertEquals(204, response.getStatusCodeValue());
        verify(accountService, times(1)).deleteAll();
    }
}
