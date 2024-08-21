package com.example.mybudget.controllers;

import com.example.mybudget.services.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/currency")
@AllArgsConstructor
public class CurrencyController {
    CurrencyService currencyService;

    @GetMapping("/conversion")
    ResponseEntity<BigDecimal> conversion(@RequestParam String from, @RequestParam String to, @RequestParam BigDecimal amount){
        return ResponseEntity.ok(currencyService.conversion(from, to, amount));
    }
    @GetMapping
    ResponseEntity<List<String>>getCurrencies() throws URISyntaxException, IOException, InterruptedException {
        return ResponseEntity.ok(currencyService.getAll());
    }
}
