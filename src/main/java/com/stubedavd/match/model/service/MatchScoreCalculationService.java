package com.stubedavd.match.model.service;

import com.stubedavd.match.model.domain.MatchScoreModel;
import com.stubedavd.player.model.domain.PlayerDomain;

public interface MatchScoreCalculationService {
    boolean isMatchFinished(MatchScoreModel matchScoreModel);
    void pointWon(MatchScoreModel matchScoreModel, PlayerDomain player);
}
