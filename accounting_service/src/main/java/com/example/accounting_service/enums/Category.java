package com.example.accounting_service.enums;

public enum Category {
    SALARY(0), POCKET(0), OTHER_EARNING(0),
    FOOD(1), MEDICINE(1), EDUCATION(1), OTHER_EXPENSE(1);

    Category(int type) {
        this.type = type;
    }

    private final int type;

    public int getType() {
        return type;
    }
}
