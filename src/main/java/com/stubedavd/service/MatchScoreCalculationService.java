package com.stubedavd.service;

import com.stubedavd.entity.Player;
import com.stubedavd.model.MatchScoreModel;

public interface MatchScoreCalculationService {

    void pointWon(MatchScoreModel matchScoreModel, Player player);

    Boolean isMatchFinished(MatchScoreModel matchScoreModel);
}
