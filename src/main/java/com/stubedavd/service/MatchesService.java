package com.stubedavd.service;

import com.stubedavd.dto.response.MatchResponseDto;
import com.stubedavd.entity.Match;

import java.util.List;

public interface MatchesService {

    Long getTotalCount();

    List<MatchResponseDto> getPage(int pageNumber, int pageSize);

    List<Match> getByPlayerName(String playerName, int pageNumber, int pageSize);
}
