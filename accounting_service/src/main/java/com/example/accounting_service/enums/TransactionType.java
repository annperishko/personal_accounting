package com.example.accounting_service.enums;

public enum TransactionType
{
    EARNING(0), EXPENSE(1);

    TransactionType(int type) {
        this.type = type;
    }

    private final int type;

    public int getType() {
        return type;
    }
}
