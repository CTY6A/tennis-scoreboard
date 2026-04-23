package com.stubedavd.logic;

public class MatchSet extends Score {

    public static final int MATCH_ADVANTAGE_LIMIT = 2;

    @Override
    protected int getAdvantageLimit() {
        return MATCH_ADVANTAGE_LIMIT;
    }
}
