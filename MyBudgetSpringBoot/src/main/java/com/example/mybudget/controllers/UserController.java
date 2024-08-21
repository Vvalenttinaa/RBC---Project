package com.example.mybudget.controllers;

import com.example.mybudget.models.dtos.User;
import com.example.mybudget.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    ResponseEntity<User> get(){
        return ResponseEntity.ok(userService.getUser());
    }
    @GetMapping("/currency")
    ResponseEntity<String>getCurrency() { return  ResponseEntity.ok(userService.getCurrency());}
    @PutMapping("currency")
    ResponseEntity<User> updateCurrency(@RequestBody String currency){
        return ResponseEntity.ok(userService.updateCurrency(currency));
    }
}
