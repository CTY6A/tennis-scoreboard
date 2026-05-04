package com.stubedavd.exception;

public class BusinessException extends RuntimeException {

    // Класс используется только в одном случае (не считая тесты) и с сообщением "Match already finished".
        // Поэтому можно дать ему более конкретное имя.

    public BusinessException(String message) {
        super(message);
    }
}
