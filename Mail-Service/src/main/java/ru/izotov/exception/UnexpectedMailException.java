package ru.izotov.exception;

public class UnexpectedMailException extends RuntimeException {
    public UnexpectedMailException(String message, Throwable cause) {
        super(message, cause);
    }
}
