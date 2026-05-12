package com.stubedavd.match.model.domain.score.value;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RegularGameScoreValue {
    ZERO("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("AD");

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
