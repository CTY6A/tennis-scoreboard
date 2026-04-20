package com.stubedavd.service;

import com.stubedavd.dto.response.MatchResponseDto;

import java.util.List;

public interface MatchesService {

    Long getTotalCount();

    Long getCountByName(String playerName);

    List<MatchResponseDto> getPage(int pageNumber, int pageSize);

    List<MatchResponseDto> getByPlayerName(String playerName, int pageNumber, int pageSize);
}
