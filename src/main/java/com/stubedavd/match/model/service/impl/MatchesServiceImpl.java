package com.stubedavd.match.model.service.impl;

import com.stubedavd.match.model.dto.response.MatchResponseDto;
import com.stubedavd.match.mapper.MatchMapper;
import com.stubedavd.match.model.repository.MatchRepository;
import com.stubedavd.match.model.service.MatchesService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MatchesServiceImpl implements MatchesService {
    private final MatchMapper matchMapper;
    private final MatchRepository matchRepository;

    public long getTotalCount() {
        return matchRepository.count();
    }

    public long getTotalCount(String playerName) {
        return matchRepository.countByPlayerName(playerName);
    }

    public List<MatchResponseDto> getPage(int pageNumber, int pageSize) {
        return matchMapper.toResponseDtoList(matchRepository.findAll(pageNumber, pageSize));
    }

    public List<MatchResponseDto> getPage(String playerName, int pageNumber, int pageSize) {
        return matchMapper.toResponseDtoList(matchRepository.findAllByPlayerName(playerName, pageNumber, pageSize));
    }
}
