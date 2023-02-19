package ru.izotov.exception;

public class UnexpectedJpaException extends RuntimeException {
    public UnexpectedJpaException(String message, Throwable cause) {
        super(message, cause);
    }
}
