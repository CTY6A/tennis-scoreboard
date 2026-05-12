package com.stubedavd.match.model.domain.score.impl;

import com.stubedavd.match.model.domain.score.Score;
import com.stubedavd.player.model.domain.PlayerDomain;

public class SetScore extends Score {

    private static final int MIN_POINTS_TO_WIN = 6;

    public SetScore(PlayerDomain player1Domain, PlayerDomain player2Domain) {
        super(player1Domain, player2Domain);
    }

    @Override
    protected int getMinPointsToWin() {
        return MIN_POINTS_TO_WIN;
    }

    @Override
    public boolean isTieBreak() {

        return super.isTieBreak();
    }
}
