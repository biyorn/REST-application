package com.epam.esm.exception;

public class FailedAddObjectException extends RuntimeException {
    public FailedAddObjectException(String message) {
        super(message);
    }

    public FailedAddObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
