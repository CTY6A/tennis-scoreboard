package com.stubedavd.logic;

public class Game extends Score {

    public static final int GAME_ADVANTAGE_LIMIT = 6;

    @Override
    protected int getAdvantageLimit() {
        return GAME_ADVANTAGE_LIMIT;
    }
}
