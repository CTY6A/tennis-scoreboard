package com.stubedavd.util;

import com.stubedavd.exception.ValidationException;

import java.math.BigDecimal;
import java.util.UUID;

public final class Validator {

    public static void validatePlayers(String playerName1, String playerName2) {

        if (playerName1 == null || playerName2 == null) {
            throw new ValidationException("Player names cannot be null");
        }

        if (playerName1.isBlank() || playerName2.isBlank()) {
            throw new ValidationException("Player names cannot be blank");
        }

        if (!playerName1.trim().matches("^[A-Za-z]+(?:[-\\s][A-Za-z]+)*$")) {
            throw new ValidationException("Invalid player name 1");
        }

        if (!playerName2.trim().matches("^[A-Za-z]+(?:[-\\s][A-Za-z]+)*$")) {
            throw new ValidationException("Invalid player name 2");
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
