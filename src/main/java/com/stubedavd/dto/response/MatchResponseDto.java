package com.stubedavd.dto.response;

import com.stubedavd.model.MatchScoreModel;

import java.util.UUID;

public record MatchResponseDto(UUID uuid, PlayerResponseDto playerResponseDto1, PlayerResponseDto playerResponseDto2, MatchScoreModel matchScoreModel) {}
