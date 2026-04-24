package com.stubedavd.model;

public class MatchSet extends MatchScore {

    public static final int MATCH_ADVANTAGE_LIMIT = 2;

    @Override
    protected int getAdvantageLimit() {
        return MATCH_ADVANTAGE_LIMIT;
    }
}
