package com.stubedavd.util;

import com.stubedavd.exception.ValidationException;

import java.util.UUID;

public final class Validator {

    // Регулярные выражения трудно читать и понимать, особенно когда они смешаны с Java-кодом. Их смысл не всегда очевиден.
        //
        // Также метод `String.matches()` при каждом вызове заново компилирует регулярное выражение. Это приводит к небольшим, но лишним затратам производительности.
        //
        // Стоит вынести каждое регулярное выражение в `private static final` константу типа `Pattern`.
        // Объект `Pattern` является скомпилированным представлением регулярного выражения и может быть переиспользован.

    // Имеет смысл добавить разумные ограничения на длину имени (которые будут согласованы с параметром length в аннотации @Column в классе Player).

    // У утилитного класса конструктор без аргументов должен быть private. Также можно использовать @UtilityClass из Lombok.

    // Вместо общего "Player name is invalid", лучше сообщать, что именно нарушено или наоборот что ожидается от имени, которое не прошло валидацию.

    public static void validatePlayers(String playerName1, String playerName2) {

        if (playerName1 == null || playerName2 == null) {
            throw new ValidationException("Player names cannot be null");
        }

        if (playerName1.isBlank() || playerName2.isBlank()) {
            throw new ValidationException("Player names cannot be blank");
        }

        if (!playerName1.trim().matches("^[A-Za-z]+(?:[-\\s][A-Za-z]+)*$")) {
            throw new ValidationException(
                    "Player 1 name should contain only Latin letters, hyphen \"-\", and whitespace"
            );
        }

        if (!playerName2.trim().matches("^[A-Za-z]+(?:[-\\s][A-Za-z]+)*$")) {
            throw new ValidationException(
                    "Player 2 name should contain only Latin letters, hyphen \"-\", and whitespace"
            );
        }

        if (playerName1.length() > 20) {
            throw new ValidationException("Player 1 name is too long");
        }

        if (playerName2.length() > 20) {
            throw new ValidationException("Player 2 name is too long");
        }
    }

    public static void validateUuid(String uuidString) {

        if (uuidString == null || uuidString.isBlank()) {

            throw new ValidationException("Uuid is invalid");
        }

        try {

            UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {

            throw new ValidationException("Invalid UUID");
        }
    }

    public static void validatePlayerId(String playerIdString) {

        if (playerIdString == null || playerIdString.isBlank()) {

            throw new ValidationException("Player id is invalid");
        }

        try {

            Integer.parseInt(playerIdString);
        } catch (NumberFormatException e) {

            throw new ValidationException("Player id is invalid");
        }
    }

    public static void validatePlayerName(String playerName) {

        if (!playerName.matches("^[A-Za-z]+(?:[-\\s][A-Za-z]+)*$")) {

            throw new ValidationException("Player name is invalid");
        }
    }

    public static void validatePageNumber(String pageNumberString) {

        try {

            Integer.parseInt(pageNumberString);
        } catch (NumberFormatException e) {

            throw new ValidationException("Page number is invalid");
        }
    }
}
