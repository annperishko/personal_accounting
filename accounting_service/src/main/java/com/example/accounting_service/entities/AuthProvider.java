package com.example.accounting_service.entities;

import lombok.Getter;

@Getter
public enum AuthProvider {
    DEFAULT("default"), GOOGLE("google");

    private final String value;

    AuthProvider(String value_p) {
        this.value = value_p;
    }
}
