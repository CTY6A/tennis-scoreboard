package com.stubedavd.logic;

public class Set extends Score {

    public static final int MATCH_ADVANTAGE_LIMIT = 2;

    @Override
    protected int getAdvantageLimit() {
        return MATCH_ADVANTAGE_LIMIT;
    }
}
