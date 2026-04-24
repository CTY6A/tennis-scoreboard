package com.stubedavd.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchPoint extends MatchScore {

    public static final int POINTS_ADVANTAGE_LIMIT = 4;
    public static final int TIE_BREAK_ADVANTAGE_LIMIT = 7;

    private boolean tieBreak;

    public MatchPoint() {

        this.tieBreak = false;
    }

    @Override
    protected int getAdvantageLimit() {
        return tieBreak ? TIE_BREAK_ADVANTAGE_LIMIT : POINTS_ADVANTAGE_LIMIT;
    }
}
