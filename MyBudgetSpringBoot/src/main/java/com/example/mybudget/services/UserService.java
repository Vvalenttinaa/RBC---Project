package com.example.mybudget.services;

import com.example.mybudget.models.dtos.User;

import java.util.UUID;

public interface UserService {
    User getUser();
    User updateCurrency(String currency);
    String getCurrency();
}
