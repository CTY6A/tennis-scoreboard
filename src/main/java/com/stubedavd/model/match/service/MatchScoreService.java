package com.stubedavd.model.match.service;

import com.stubedavd.model.match.dto.response.FinalScoreResponseDto;
import com.stubedavd.model.match.dto.response.MatchScoreResponseDto;

import java.util.UUID;

public interface MatchScoreService {
    MatchScoreResponseDto getScore(UUID uuid);
    boolean isMatchFinished(UUID uuid);
    void pointWon(UUID uuid, Long playerId);
    FinalScoreResponseDto recordMatch(UUID uuid);
}
