package com.example.mybudget.controllers;
import com.example.mybudget.services.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CurrencyControllerTest {
    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConversion() {
        // Arrange
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal expectedConversion = BigDecimal.valueOf(85);

        when(currencyService.conversion(fromCurrency, toCurrency, amount)).thenReturn(expectedConversion);

        ResponseEntity<BigDecimal> response = currencyController.conversion(fromCurrency, toCurrency, amount);

        assertEquals(ResponseEntity.ok(expectedConversion), response);
    }

    @Test
    void testGetCurrencies() throws URISyntaxException, IOException, InterruptedException {
        List<String> expectedCurrencies = Arrays.asList("USD", "EUR", "GBP");

        when(currencyService.getAll()).thenReturn(expectedCurrencies);

        ResponseEntity<List<String>> response = currencyController.getCurrencies();

        assertEquals(ResponseEntity.ok(expectedCurrencies), response);
    }
}
