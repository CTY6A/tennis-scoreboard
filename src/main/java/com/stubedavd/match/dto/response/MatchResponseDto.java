package com.stubedavd.match.dto.response;

public record MatchResponseDto(
        String player1Name,
        String player2Name,
        String winnerName
) {
}
