package com.example.mybudget.controllers;
import com.example.mybudget.models.dtos.User;
import com.example.mybudget.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void get() {
        User user = new User();
        when(userService.getUser()).thenReturn(user);

        ResponseEntity<User> response = userController.get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).getUser();
    }

    @Test
    void getCurrency() {
        String currency = "USD";
        when(userService.getCurrency()).thenReturn(currency);

        ResponseEntity<String> response = userController.getCurrency();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(currency, response.getBody());
        verify(userService, times(1)).getCurrency();
    }

    @Test
    void updateCurrency() {
        String currency = "EUR";
        User updatedUser = new User();

        when(userService.updateCurrency(currency)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.updateCurrency(currency);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService, times(1)).updateCurrency(currency);
    }
}
