package com.example.mybudget.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class CurrencyInEUR {
    String name;
    BigDecimal value;
}
