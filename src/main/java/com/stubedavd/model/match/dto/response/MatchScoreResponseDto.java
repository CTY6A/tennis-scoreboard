package com.stubedavd.model.match.dto.response;

import com.stubedavd.model.player.dto.response.PlayerResponseDto;

public record MatchScoreResponseDto(PlayerResponseDto player1, PlayerResponseDto player2) {
}
