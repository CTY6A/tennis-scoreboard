package com.stubedavd.match.model.service;

import com.stubedavd.player.model.domain.PlayerDomain;
import com.stubedavd.exception.BusinessException;
import com.stubedavd.match.model.domain.MatchScoreModel;

public class MatchScoreCalculationService {

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
