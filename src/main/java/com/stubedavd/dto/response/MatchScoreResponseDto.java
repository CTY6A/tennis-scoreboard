package com.stubedavd.dto.response;


public record MatchScoreResponseDto(
        PlayerScoreResponseDto playerScoreResponseDto1,
        PlayerScoreResponseDto playerScoreResponseDto2
) {}
