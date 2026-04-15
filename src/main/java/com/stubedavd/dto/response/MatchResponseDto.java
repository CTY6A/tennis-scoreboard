package com.stubedavd.dto.response;

public record MatchResponseDto(
        String player1Name,
        String player2Name,
        String winnerName
) {
}
