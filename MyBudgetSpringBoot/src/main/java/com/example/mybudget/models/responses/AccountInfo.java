package com.example.mybudget.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class AccountInfo {
    String id;
    String name;
    String currency;
    public AccountInfo(UUID id, String name, String currency){
        this.id=id.toString();
        this.name=name;
        this.currency= currency;
    }
}
