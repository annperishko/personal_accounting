package com.example.accounting_service.enums;

import lombok.Getter;

@Getter
public enum Category {
    SALARY(TransactionType.EARNING), POCKET(TransactionType.EARNING), OTHER_EARNING(TransactionType.EARNING),
    FOOD(TransactionType.EXPENSE), MEDICINE(TransactionType.EXPENSE), EDUCATION(TransactionType.EXPENSE), OTHER_EXPENSE(TransactionType.EXPENSE);

    Category(TransactionType type) {
        this.type = type;
    }

    private final TransactionType type;

}
