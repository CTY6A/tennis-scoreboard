package com.stubedavd.match.service;

import com.stubedavd.match.entity.Match;
import com.stubedavd.match.mapper.MatchMapper;
import com.stubedavd.match.model.MatchScoreModel;
import com.stubedavd.match.repository.MatchRepository;

public class FinishedMatchesPersistenceService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    private final MatchMapper matchMapper;

    private final MatchRepository matchRepository;

    // можно использовать @RequiredArgsConstructor над классом вместо самописного конструктора
    public FinishedMatchesPersistenceService(
            MatchMapper matchMapper,
            MatchRepository matchRepository
    ) {

        this.matchMapper = matchMapper;
        this.matchRepository = matchRepository;
    }

    public void recordMatch(MatchScoreModel matchScoreModel) {

        Match match = matchMapper.toModel(
                matchScoreModel.getPlayer1(),
                matchScoreModel.getPlayer2(),
                matchScoreModel.getWinner()
        );

        matchRepository.save(match);
    }
}
