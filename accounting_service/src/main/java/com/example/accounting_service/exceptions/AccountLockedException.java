package com.example.accounting_service.exceptions;

public class AccountLockedException extends RuntimeException {
    private final long lockTimeRemainingSeconds;

    public AccountLockedException(String message, long lockTimeRemainingSeconds) {
        super(message);
        this.lockTimeRemainingSeconds = lockTimeRemainingSeconds;
    }

    public long getLockTimeRemainingSeconds() {
        return lockTimeRemainingSeconds;
    }

    public String getFormattedLockTime() {
        long minutes = lockTimeRemainingSeconds / 60;
        long seconds = lockTimeRemainingSeconds % 60;
        return String.format("%d minutes, %d seconds", minutes, seconds);
    }}
