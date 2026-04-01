package com.stubedavd.dto;

import com.stubedavd.model.entity.Player;

import java.util.UUID;

public record MatchDto(UUID uuid, Player player1, Player player2, MatchScoreDto matchScoreDto) {}
