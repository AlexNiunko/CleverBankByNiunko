package com.example.cleverbankbyniunko.exception;

public class AccountVerifierException extends Exception{
    public AccountVerifierException() {
        super();
    }

    public AccountVerifierException(String message) {
        super(message);
    }

    public AccountVerifierException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountVerifierException(Throwable cause) {
        super(cause);
    }

    protected AccountVerifierException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
