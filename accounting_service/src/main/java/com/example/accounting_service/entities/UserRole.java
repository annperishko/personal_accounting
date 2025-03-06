package com.example.accounting_service.entities;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN(1), USER(2);

    private final Integer value;

    UserRole(Integer value_p) {
        this.value = value_p;
    }

    public static UserRole fromValue(Integer value) {
        for (UserRole role : UserRole.values()) {
            if (role.getValue().equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No UserRole with value " + value + " found");
    }
}
