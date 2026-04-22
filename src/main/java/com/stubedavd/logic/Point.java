package com.stubedavd.logic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Point extends Score{

    public static final int POINTS_ADVANTAGE_LIMIT = 4;
    public static final int TIE_BREAK_ADVANTAGE_LIMIT = 7;

    private boolean tieBrake = false;

    @Override
    protected int getAdvantageLimit() {
        return tieBrake ? TIE_BREAK_ADVANTAGE_LIMIT : POINTS_ADVANTAGE_LIMIT;
    }
}
