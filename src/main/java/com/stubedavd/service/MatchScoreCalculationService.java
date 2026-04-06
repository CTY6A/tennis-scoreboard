package com.stubedavd.service;

import com.stubedavd.dto.PlayerDto;
import com.stubedavd.model.MatchScoreModel;

public interface MatchScoreCalculationService {

    void pointWon(MatchScoreModel matchScoreModel, PlayerDto playerDto);

    Boolean isMatchFinished(MatchScoreModel matchScoreModel);
}
