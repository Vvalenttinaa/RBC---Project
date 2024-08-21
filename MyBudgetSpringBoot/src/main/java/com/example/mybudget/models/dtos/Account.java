package com.example.mybudget.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private String id;
    private String name;
    private String currency;
    private Double balance;
    private Byte deleted;
}
