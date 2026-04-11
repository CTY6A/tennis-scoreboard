package com.stubedavd.service;

import com.stubedavd.dto.response.MatchScoreResponseDto;

import java.util.UUID;

public interface MatchScoreService {

    MatchScoreResponseDto getMatchScore(UUID uuid);

    void pointWon(UUID uuid, Integer playerId);
}
