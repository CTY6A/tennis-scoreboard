package com.stubedavd.match.model.service;

import com.stubedavd.match.model.dto.response.MatchResponseDto;
import com.stubedavd.match.mapper.MatchMapper;
import com.stubedavd.match.model.repository.MatchRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MatchesService {
    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)
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
