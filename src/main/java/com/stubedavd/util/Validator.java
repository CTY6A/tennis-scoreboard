package com.stubedavd.util;

import com.stubedavd.exception.ValidationException;

import java.math.BigDecimal;
import java.util.UUID;

public final class Validator {

    public static final int ONE_CODE_AND_SLASH = 4;
    public static final int TWO_CODES_AND_SLASH = 7;

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

    public static void validateCurrency(String name, String code, String sign) {

        if (name == null || name.isBlank()) {
            throw new ValidationException("Name is invalid");
        }

        if (code == null || !code.trim().matches("[A-Za-z]{3}")) {
            throw new ValidationException("Code is invalid");
        }

        if (sign == null || sign.isBlank() || sign.trim().length() > 3 ) {
            throw new ValidationException("Sign is invalid");
        }
    }

    public static void validateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, String rate) {

        if (baseCurrencyCode == null || !baseCurrencyCode.trim().matches("[A-Za-z]{3}")) {
            throw new ValidationException("Base currency code is invalid");
        }

        if (targetCurrencyCode == null || !targetCurrencyCode.trim().matches("[A-Za-z]{3}")) {
            throw new ValidationException("Target currency code is invalid");
        }

        try {
            new BigDecimal(rate);
        } catch (NullPointerException | NumberFormatException e) {
            throw new ValidationException("Input number is invalid");
        }
    }

    public static void validateOneCodePath(String pathInfo) {

        if (pathInfo == null || pathInfo.trim().length() != ONE_CODE_AND_SLASH) {
            throw new ValidationException("A required form field is missing");
        }
    }

    public static void validateTwoCodesPath(String pathInfo) {

        if (pathInfo == null || pathInfo.trim().length() != TWO_CODES_AND_SLASH) {
            throw new ValidationException("A required form field is missing");
        }
    }

    public static void validateRateParameter(String rateParameter) {

        if (rateParameter == null || rateParameter.isBlank()) {
            throw new ValidationException("Rate parameter is invalid");
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
