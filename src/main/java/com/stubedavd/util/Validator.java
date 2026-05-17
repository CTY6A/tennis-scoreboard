package com.stubedavd.util;

import com.stubedavd.exception.ValidationException;
import com.stubedavd.player.model.entity.Player;
import lombok.experimental.UtilityClass;

import java.util.UUID;
import java.util.regex.Pattern;

@UtilityClass
public final class Validator {
    private static final Pattern FULL_NAME_REGEX = Pattern.compile("^[A-Za-z]+(?:[-\\s][A-Za-z]+)*$");

    public static void validatePlayers(String playerName1, String playerName2) {
        if (playerName1 == null || playerName2 == null) {
            throw new ValidationException("Player names cannot be null");
        }
        if (playerName1.isBlank() || playerName2.isBlank()) {
            throw new ValidationException("Player names cannot be blank");
        }
        if (!FULL_NAME_REGEX.matcher(playerName1.trim()).matches()) {
            throw new ValidationException(
                    "Player 1 name should contain only Latin letters, hyphen \"-\", and whitespace"
            );
        }
        if (!FULL_NAME_REGEX.matcher(playerName2.trim()).matches()) {
            throw new ValidationException(
                    "Player 2 name should contain only Latin letters, hyphen \"-\", and whitespace"
            );
        }
        if (playerName1.length() > Player.NAME_MAX_LENGTH) {
            throw new ValidationException("Player 1 name is too long");
        }
        if (playerName2.length() > Player.NAME_MAX_LENGTH) {
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
        if (!FULL_NAME_REGEX.matcher(playerName).matches()) {
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
