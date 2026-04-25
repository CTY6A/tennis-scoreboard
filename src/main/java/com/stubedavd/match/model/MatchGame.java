package com.stubedavd.match.model;

public class MatchGame extends MatchScore {

    public static final int GAME_ADVANTAGE_LIMIT = 6;

    @Override
    protected int getAdvantageLimit() {
        return GAME_ADVANTAGE_LIMIT;
    }

    @Override
    public boolean checkTieBreak() {

        return super.checkTieBreak();
    }
}
