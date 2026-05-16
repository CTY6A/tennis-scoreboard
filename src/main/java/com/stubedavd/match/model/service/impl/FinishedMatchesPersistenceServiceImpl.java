package com.stubedavd.match.model.service.impl;

import com.stubedavd.match.model.entity.Match;
import com.stubedavd.match.mapper.MatchMapper;
import com.stubedavd.match.model.domain.MatchScoreModel;
import com.stubedavd.match.model.repository.MatchRepository;
import com.stubedavd.match.model.service.FinishedMatchesPersistenceService;
import com.stubedavd.player.mapper.PlayerMapper;
import com.stubedavd.player.model.entity.Player;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FinishedMatchesPersistenceServiceImpl implements FinishedMatchesPersistenceService {
    private final PlayerMapper playerMapper;
    private final MatchMapper matchMapper;

    private final MatchRepository matchRepository;

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
