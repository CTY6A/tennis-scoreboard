package com.stubedavd.match.model.service;

import com.stubedavd.match.model.entity.Match;
import com.stubedavd.match.mapper.MatchMapper;
import com.stubedavd.match.model.domain.MatchScoreModel;
import com.stubedavd.match.model.repository.MatchRepository;
import com.stubedavd.match.model.repository.impl.MatchRepositoryImpl;
import com.stubedavd.player.mapper.PlayerMapper;
import com.stubedavd.player.model.entity.Player;

public class FinishedMatchesPersistenceService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    private final PlayerMapper playerMapper;
    private final MatchMapper matchMapper;

    private final MatchRepository matchRepository;

    // можно использовать @RequiredArgsConstructor над классом вместо самописного конструктора
    public FinishedMatchesPersistenceService(
            PlayerMapper playerMapper,
            MatchMapper matchMapper,
            MatchRepository matchRepository
    ) {

        this.playerMapper = playerMapper;
        this.matchMapper = matchMapper;
        this.matchRepository = matchRepository;
    }

    public void recordMatch(MatchScoreModel matchScoreModel) {

        Player player1 = playerMapper.toEntity(matchScoreModel.getPlayer1());
        Player player2 = playerMapper.toEntity(matchScoreModel.getPlayer2());
        Player winner = playerMapper.toEntity(matchScoreModel.getWinner());

        Match match = matchMapper.toEntity(
                player1,
                player2,
                winner
        );

        matchRepository.save(match);
    }
}
