package com.stubedavd.model.match.service.impl;

import com.stubedavd.model.match.service.MatchScoreCalculationService;
import com.stubedavd.model.player.domain.PlayerDomain;
import com.stubedavd.exception.BusinessException;
import com.stubedavd.model.match.domain.MatchScoreModel;

public class MatchScoreCalculationServiceImpl implements MatchScoreCalculationService {
    public boolean isMatchFinished(MatchScoreModel matchScoreModel) {
        return matchScoreModel.getMatchScore().isRoundFinished();
    }

    public void pointWon(MatchScoreModel matchScoreModel, PlayerDomain player) {
        if (isMatchFinished(matchScoreModel)) {
            throw new BusinessException("Match already finished");
        }
        matchScoreModel.pointWon(player);
    }
}
