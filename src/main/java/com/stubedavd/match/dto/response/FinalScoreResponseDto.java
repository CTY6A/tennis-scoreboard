package com.stubedavd.match.dto.response;

import com.stubedavd.player.dto.response.PlayerFinalScoreResponseDto;

public record FinalScoreResponseDto(
        PlayerFinalScoreResponseDto player1FinalScoreResponseDto,
        PlayerFinalScoreResponseDto player2FinalScoreResponseDto
) {
}
