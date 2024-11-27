package com.github.victor.stockms.web.exceptions;

public class UniqueEntityException extends RuntimeException {
    public UniqueEntityException(String message) {
        super(message);
    }
}
