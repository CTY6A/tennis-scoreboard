package com.stubedavd.dto;

import java.util.UUID;

public record MatchDto(UUID uuid, PlayerDto playerDto1, PlayerDto playerDto2, MatchScoreDto matchScoreDto) {}
