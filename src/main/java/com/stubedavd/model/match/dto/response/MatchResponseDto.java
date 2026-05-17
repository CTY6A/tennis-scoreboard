package com.stubedavd.model.match.dto.response;

public record MatchResponseDto(
        String player1Name,
        String player2Name,
        String winnerName
) {
}
