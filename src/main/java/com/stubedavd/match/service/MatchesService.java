package com.stubedavd.match.service;

import com.stubedavd.match.dto.response.MatchResponseDto;
import com.stubedavd.match.mapper.MatchMapper;
import com.stubedavd.match.repository.MatchRepository;

import java.util.List;

public class MatchesService {

    MatchMapper matchMapper;

    MatchRepository matchRepository;

    public MatchesService(
            MatchMapper matchMapper,
            MatchRepository matchRepository
    ) {

        this.matchMapper = matchMapper;
        this.matchRepository = matchRepository;
    }

    public long getTotalCount() {

        return matchRepository.findTotalCount();
    }

    public long getTotalCount(String playerName) {

        return matchRepository.findCountByName(playerName);
    }

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

    public List<MatchResponseDto> getPage(String playerName, int pageNumber, int pageSize) {

        return matchRepository
                .findByPlayerName(playerName, pageNumber, pageSize)
                .stream()
                .map(match -> matchMapper.toResponseDto(
                        match.getPlayer1().getName(),
                        match.getPlayer2().getName(),
                        match.getWinner().getName()
                ))
                .toList();
    }
}
