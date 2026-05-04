package com.stubedavd.exception;

public class AlreadyExistException extends RuntimeException {

    // Можно переименовать в EntityAlreadyExistException

    public AlreadyExistException(String message) {
        super(message);
    }
}
