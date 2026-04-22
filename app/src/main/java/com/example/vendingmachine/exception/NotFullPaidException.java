package com.example.vendingmachine.exception;

public class NotFullPaidException extends RuntimeException {
    public NotFullPaidException(String message) {
        super(message);
    }
}