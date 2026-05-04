package com.stubedavd.exception;

public class DatabaseException extends RuntimeException {

    // Так как класс оборачивает исключения от БД, имеет смысл добавить ему конструктор public DatabaseException(String message, Throwable cause).
    // Исходное исключение может пригодиться в отладке.

    public DatabaseException(String message) {
        super(message);
    }
}
