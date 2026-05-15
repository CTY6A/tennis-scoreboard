package com.stubedavd.match.model.service;

import com.stubedavd.match.model.dto.response.MatchResponseDto;
import com.stubedavd.match.mapper.MatchMapper;
import com.stubedavd.match.model.repository.MatchRepository;

import java.util.List;

public class MatchesService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    private final MatchMapper matchMapper;

    private final MatchRepository matchRepository;

    // можно использовать @RequiredArgsConstructor над классом вместо самописного конструктора
    public MatchesService(
            MatchMapper matchMapper,
            MatchRepository matchRepository
    ) {

        this.matchMapper = matchMapper;
        this.matchRepository = matchRepository;
    }

    public long getTotalCount() {

        return matchRepository.count();
    }

    public long getTotalCount(String playerName) {

        return matchRepository.countByPlayerName(playerName);
    }

    public List<MatchResponseDto> getPage(int pageNumber, int pageSize) {

        return matchRepository
                .findAll(pageNumber, pageSize)
                .stream()

                // Метод, принимающий List<Match> и возвращающий List<MatchResponseDto> можно добавить в MatchMapper и перенести эту логику в него.
                .map(match -> matchMapper.toResponseDto(
                        match.getPlayer1().getName(),
                        match.getPlayer2().getName(),
                        match.getWinner().getName()
                ))
                .toList();
    }

    public List<MatchResponseDto> getPage(String playerName, int pageNumber, int pageSize) {

        return matchRepository
                .findAllByPlayerName(playerName, pageNumber, pageSize)
                .stream()

                // Метод, принимающий List<Match> и возвращающий List<MatchResponseDto> можно добавить в MatchMapper и перенести эту логику в него.
                .map(match -> matchMapper.toResponseDto(
                        match.getPlayer1().getName(),
                        match.getPlayer2().getName(),
                        match.getWinner().getName()
                ))
                .toList();
    }
}
