package com.stubedavd.model.match.service.impl;

import com.stubedavd.model.match.dto.response.MatchResponseDto;
import com.stubedavd.mapper.match.MatchMapper;
import com.stubedavd.model.match.dto.response.MatchesResponseDto;
import com.stubedavd.model.match.repository.MatchRepository;
import com.stubedavd.model.match.service.MatchesService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MatchesServiceImpl implements MatchesService {
    private static final int PAGE_SIZE = 10;

    private final MatchMapper matchMapper;
    private final MatchRepository matchRepository;

    @Override
    public MatchesResponseDto getMatches(String playerName, long pageNumber) {
        List<MatchResponseDto> matches;
        long pageCount;
        long matchesCount;
        if (playerName.isBlank()) {
            matchesCount = matchRepository.count();
        } else {
            matchesCount = matchRepository.countByPlayerName(playerName);
        }
        pageCount = (matchesCount + PAGE_SIZE - 1) / PAGE_SIZE;
        if (pageNumber > pageCount - 1 && pageCount != 0) {
            pageNumber = pageCount - 1;
        }
        if (playerName.isBlank()) {
            matches = matchMapper.toResponseDtoList(matchRepository.findAll((int) pageNumber, PAGE_SIZE));
        } else {
            matches = matchMapper.toResponseDtoList(
                    matchRepository.findAllByPlayerName(playerName, (int) pageNumber, PAGE_SIZE)
            );
        }
        return matchMapper.toMatchesResponseDto(
                matches,
                playerName,
                pageCount,
                pageNumber + 1,
                matchesCount,
                pageNumber * PAGE_SIZE,
                pageNumber * PAGE_SIZE + matches.size()
        );
    }
}
