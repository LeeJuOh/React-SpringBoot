package com.example.gccoffeeclone.exception;

public class ObjectNotExistException extends RuntimeException {

    public ObjectNotExistException(String message) {
        super(message);
    }
}