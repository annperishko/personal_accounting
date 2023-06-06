package com.example.accounting_service.exceptions;

public class NotCompatibleCategory extends RuntimeException {
    public NotCompatibleCategory(String message) {
        super(message);
    }

}
