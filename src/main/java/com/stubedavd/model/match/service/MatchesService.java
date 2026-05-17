package com.stubedavd.model.match.service;

import com.stubedavd.model.match.dto.response.MatchResponseDto;

import java.util.List;

public interface MatchesService {
    long getTotalCount();
    long getTotalCount(String playerName);
    List<MatchResponseDto> getPage(int pageNumber, int pageSize);
    List<MatchResponseDto> getPage(String playerName, int pageNumber, int pageSize);
}
