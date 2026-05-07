package com.stubedavd.match.model.dto.response;

public record MatchResponseDto(
        String player1Name,
        String player2Name,
        String winnerName
) {
}
