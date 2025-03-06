package com.example.accounting_service.exceptions;

import org.springframework.security.core.AuthenticationException;

public class OAuthAuthenticationException extends AuthenticationException {
    public OAuthAuthenticationException(String msg) {
        super(msg);
    }

    public OAuthAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}