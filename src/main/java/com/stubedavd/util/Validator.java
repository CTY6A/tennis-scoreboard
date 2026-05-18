package com.stubedavd.util;

import com.stubedavd.exception.ValidationException;
import com.stubedavd.model.player.entity.Player;
import lombok.experimental.UtilityClass;

import java.util.UUID;
import java.util.regex.Pattern;

@UtilityClass
public final class Validator {
    public static final Pattern FULL_NAME_REGEX = Pattern.compile("^[A-Za-z]+(?:[-\\s][A-Za-z]+)*$");
    public static final String PLAYER_NAMES_CANNOT_BE_NULL = "Player names cannot be null";
    public static final String PLAYER_NAMES_CANNOT_BE_BLANK = "Player names cannot be blank";
    public static final String PLAYER_1_VALIDATION_RULES
            = "Player 1 name should contain only Latin letters, hyphen \"-\", and whitespace";
    public static final String PLAYER_2_VALIDATION_RULES
            = "Player 2 name should contain only Latin letters, hyphen \"-\", and whitespace";
    public static final String PLAYER_1_NAME_IS_TOO_LONG = "Player 1 name is too long";
    public static final String PLAYER_2_NAME_IS_TOO_LONG = "Player 2 name is too long";
    public static final String UUID_IS_INVALID = "Uuid is invalid";
    public static final String PLAYER_ID_IS_INVALID = "Player id is invalid";
    public static final String PLAYER_NAME_IS_INVALID = "Player name is invalid";
    public static final String PAGE_NUMBER_IS_INVALID = "Page number is invalid";

    public static void validatePlayers(String playerName1, String playerName2) {
        if (playerName1 == null || playerName2 == null) {
            throw new ValidationException(PLAYER_NAMES_CANNOT_BE_NULL);
        }
        if (playerName1.isBlank() || playerName2.isBlank()) {
            throw new ValidationException(PLAYER_NAMES_CANNOT_BE_BLANK);
        }
        if (!FULL_NAME_REGEX.matcher(playerName1.trim()).matches()) {
            throw new ValidationException(
                    PLAYER_1_VALIDATION_RULES
            );
        }
        if (!FULL_NAME_REGEX.matcher(playerName2.trim()).matches()) {
            throw new ValidationException(
                    PLAYER_2_VALIDATION_RULES
            );
        }
        if (playerName1.length() > Player.NAME_MAX_LENGTH) {
            throw new ValidationException(PLAYER_1_NAME_IS_TOO_LONG);
        }
        if (playerName2.length() > Player.NAME_MAX_LENGTH) {
            throw new ValidationException(PLAYER_2_NAME_IS_TOO_LONG);
        }
    }

    public static void validateUuid(String uuidString) {
        if (uuidString == null || uuidString.isBlank()) {
            throw new ValidationException(UUID_IS_INVALID);
        }
        try {
            UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(UUID_IS_INVALID);
        }
    }

    public static void validatePlayerId(String playerIdString) {
        if (playerIdString == null || playerIdString.isBlank()) {
            throw new ValidationException(PLAYER_ID_IS_INVALID);
        }
        try {
            Integer.parseInt(playerIdString);
        } catch (NumberFormatException e) {
            throw new ValidationException(PLAYER_ID_IS_INVALID);
        }
    }

    public static void validatePlayerName(String playerName) {
        if (!FULL_NAME_REGEX.matcher(playerName).matches()) {
            throw new ValidationException(PLAYER_NAME_IS_INVALID);
        }
    }

    public static void validatePageNumber(String pageNumberString) {
        try {
            Integer.parseInt(pageNumberString);
        } catch (NumberFormatException e) {
            throw new ValidationException(PAGE_NUMBER_IS_INVALID);
        }
    }
}
