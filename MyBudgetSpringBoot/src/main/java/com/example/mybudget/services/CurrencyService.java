package com.example.mybudget.services;

import com.example.mybudget.models.dtos.Currency;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

public interface CurrencyService {
    BigDecimal convertToEuro(Currency currency);
    public BigDecimal convertFromEuro(Currency currency);
    public BigDecimal conversion(String from, String to, BigDecimal number);
//    Integer compare(Currency currency1, Currency currency2);
    List<String> getAll() throws URISyntaxException, IOException, InterruptedException;
    }
