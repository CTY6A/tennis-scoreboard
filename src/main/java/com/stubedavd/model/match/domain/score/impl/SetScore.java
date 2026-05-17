package com.stubedavd.model.match.domain.score.impl;

import com.stubedavd.model.match.domain.score.Score;
import com.stubedavd.model.player.domain.PlayerDomain;

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
