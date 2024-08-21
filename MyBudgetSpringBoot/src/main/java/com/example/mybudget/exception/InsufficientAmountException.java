package com.example.mybudget.exception;

public class InsufficientAmountException extends RuntimeException{
    public InsufficientAmountException()
    {
        super("Insufficient amount on the account!");
    }

    public InsufficientAmountException(String message)
    {
        super(message);
    }
}
