package com.stubedavd.service;

import com.stubedavd.dto.response.MatchScoreResponseDto;

import java.util.UUID;

public interface MatchScoreService {

    MatchScoreResponseDto getMatchScore(UUID uuid);

    void playerScore(UUID uuid, Integer playerId);

    boolean isMatchFinished(UUID uuid);
}
