package com.stubedavd.model.match.service;

import com.stubedavd.model.match.domain.MatchScoreModel;
import com.stubedavd.model.player.domain.PlayerDomain;

public interface MatchScoreCalculationService {
    boolean isMatchFinished(MatchScoreModel matchScoreModel);
    void pointWon(MatchScoreModel matchScoreModel, PlayerDomain player);
}
