package com.example.mybudget.controllers;

import com.example.mybudget.models.dtos.Account;
import com.example.mybudget.models.dtos.Currency;
import com.example.mybudget.models.requests.AccountRequest;
import com.example.mybudget.models.responses.AccountInfo;
import com.example.mybudget.services.AccountService;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostConstruct
    private void readOldData() throws JAXBException, IOException {
        accountService.readOldData();
    }

    @GetMapping
    ResponseEntity<List<Account>>getAll(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size){
        return ResponseEntity.ok(accountService.getAll(PageRequest.of(page,size)));
    }

    @GetMapping("/total")
    public ResponseEntity<Currency> getTotalAmount() {
        Currency totalBalance = accountService.getTotalBalance();
        return ResponseEntity.ok(totalBalance);
    }

    @GetMapping("/info")
    public ResponseEntity<List<AccountInfo>>getAccountInfo(){
        return ResponseEntity.ok(accountService.getAllAccountInfo());
    }

    @GetMapping("/name")
    public ResponseEntity<List<String>>getAccountNames(){
        return ResponseEntity.ok(accountService.getAllNames());
    }

    @GetMapping("/currency/{id}")
    public ResponseEntity<String>getCurrency(@PathVariable String id){
        return ResponseEntity.ok(accountService.getCurrency(id));
    }

    @PostMapping
    ResponseEntity<Account> insert(@RequestBody AccountRequest accountRequest){
        return ResponseEntity.ok(accountService.insert(accountRequest));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() {
        accountService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
