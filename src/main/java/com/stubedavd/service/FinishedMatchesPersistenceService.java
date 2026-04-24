package com.stubedavd.service;

import com.stubedavd.dto.OngoingMatchDto;
import com.stubedavd.entity.Match;
import com.stubedavd.mapper.MatchMapper;
import com.stubedavd.repository.MatchRepository;

public class FinishedMatchesPersistenceService {

    OngoingMatchService ongoingMatchService;

    MatchMapper matchMapper;

    MatchRepository matchRepository;

    public FinishedMatchesPersistenceService(
            OngoingMatchService ongoingMatchService,
            MatchMapper matchMapper,
            MatchRepository matchRepository
    ) {

        this.ongoingMatchService = ongoingMatchService;
        this.matchMapper = matchMapper;
        this.matchRepository = matchRepository;
    }

    public void recordMatch(OngoingMatchDto ongoingMatchDto) {

        Match match = matchMapper.toModel(
                ongoingMatchDto.player1(),
                ongoingMatchDto.player2(),
                ongoingMatchDto.matchScoreModel().getWinner()
        );

        matchRepository.save(match);
    }
}
