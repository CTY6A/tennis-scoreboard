package com.stubedavd.match.model.domain.score.impl;

import com.stubedavd.match.model.domain.score.Score;
import com.stubedavd.player.model.domain.PlayerDomain;

public class TiebreakScore extends Score {

    private static final int MIN_POINTS_TO_WIN = 7;

    public TiebreakScore(PlayerDomain player1Domain, PlayerDomain player2Domain) {
        super(player1Domain, player2Domain);
    }

    @Override
    protected int getMinPointsToWin() {
        return MIN_POINTS_TO_WIN;
    }
}
