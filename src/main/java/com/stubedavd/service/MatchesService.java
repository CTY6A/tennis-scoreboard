package com.stubedavd.service;

import com.stubedavd.dto.response.MatchResponseDto;
import com.stubedavd.mapper.MatchMapper;
import com.stubedavd.repository.MatchRepository;

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

    public Long getTotalCount() {

        return matchRepository.findTotalCount();
    }

    public Long getTotalCount(String playerName) {

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
