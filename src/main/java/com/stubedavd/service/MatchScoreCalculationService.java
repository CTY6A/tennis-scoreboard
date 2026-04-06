package com.stubedavd.service;

import com.stubedavd.dto.PlayerDto;

public interface MatchScoreCalculationService {

    void pointWon(PlayerDto playerDto);

    boolean isMatchFinished();
}
