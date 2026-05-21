package com.stubedavd.model.match.service;

import com.stubedavd.model.match.dto.response.MatchesResponseDto;

public interface MatchesService {
    MatchesResponseDto getMatches(String playerName, long pageNumber);
}
