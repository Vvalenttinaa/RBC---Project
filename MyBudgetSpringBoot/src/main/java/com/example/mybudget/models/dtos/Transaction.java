package com.example.mybudget.models.dtos;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String id;
    private String amount;
    private String description;
    private String accountId;
    private String accountName;
    private String accountCurrency;
    private Byte deleted;
}
