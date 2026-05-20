package com.stubedavd.model.player.dto.response;

import java.util.List;

public record PlayerScoreResponseDto(String name, List<Integer> score) {
}
