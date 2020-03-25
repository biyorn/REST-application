package com.epam.esm.exception;

public class FailedUpdateObjectException extends RuntimeException {

    public FailedUpdateObjectException() {
    }

    public FailedUpdateObjectException(String message) {
        super(message);
    }
}
