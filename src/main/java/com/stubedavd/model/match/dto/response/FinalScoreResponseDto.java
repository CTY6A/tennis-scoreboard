package com.stubedavd.model.match.dto.response;

import com.stubedavd.model.player.dto.response.PlayerScoreResponseDto;

public record FinalScoreResponseDto(PlayerScoreResponseDto player1, PlayerScoreResponseDto player2) {
}
