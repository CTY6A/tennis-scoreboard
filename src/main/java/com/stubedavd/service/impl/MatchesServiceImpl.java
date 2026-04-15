package com.stubedavd.service.impl;

import com.stubedavd.dto.response.MatchResponseDto;
import com.stubedavd.entity.Match;
import com.stubedavd.mapper.MatchMapper;
import com.stubedavd.repository.MatchRepository;
import com.stubedavd.service.MatchesService;

import java.util.List;

public class MatchesServiceImpl implements MatchesService {

    MatchMapper matchMapper;

    MatchRepository matchRepository;

    public MatchesServiceImpl(
            MatchMapper matchMapper,
            MatchRepository matchRepository
    ) {

        this.matchMapper = matchMapper;
        this.matchRepository = matchRepository;
    }

    @Override
    public Long getTotalCount() {

        return matchRepository.findTotalCount();
    }

    @Override
    public List<MatchResponseDto> getPage(int pageNumber, int pageSize) {

        return matchRepository
                .findPage(pageNumber, pageSize)
                .stream()
                .map(match -> matchMapper.toResponseDto(
                        match.getPlayer1().getName(),
                        match.getPlayer2().getName(),
                        match.getWinner().getName()
                ))
                .toList();
    }

    @Override
    public List<Match> getByPlayerName(String playerName, int pageNumber, int pageSize) {

        return matchRepository.findByPlayerName(playerName, pageNumber, pageSize);
    }
}
