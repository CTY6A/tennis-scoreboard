package com.stubedavd.match.model.service;

import com.stubedavd.match.model.dto.response.MatchResponseDto;

import java.util.List;

public interface MatchesService {
    long getTotalCount();
    long getTotalCount(String playerName);
    List<MatchResponseDto> getPage(int pageNumber, int pageSize);
    List<MatchResponseDto> getPage(String playerName, int pageNumber, int pageSize);
}
