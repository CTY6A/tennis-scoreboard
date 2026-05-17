package com.stubedavd.model.match.service.impl;

import com.stubedavd.model.match.dto.response.MatchResponseDto;
import com.stubedavd.mapper.match.MatchMapper;
import com.stubedavd.model.match.repository.MatchRepository;
import com.stubedavd.model.match.service.MatchesService;
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
