package com.example.mybudget.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException()
    {
        super("Not found.");
    }

    public NotFoundException(String message)
    {
        super(message);
    }
}
