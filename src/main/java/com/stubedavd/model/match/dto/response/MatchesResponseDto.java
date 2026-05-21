package com.stubedavd.model.match.dto.response;

import java.util.List;

public record MatchesResponseDto(
        List<MatchResponseDto> matches,
        String playerName,
        long pageCount,
        long pageNumber,
        long matchesCount,
        long matchesFrom,
        long matchesTo
) {}
