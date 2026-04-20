package com.stubedavd.dto.response;

public record FinalScoreResponseDto(
        PlayerFinalScoreResponseDto player1FinalScoreResponseDto,
        PlayerFinalScoreResponseDto player2FinalScoreResponseDto
) {
}
