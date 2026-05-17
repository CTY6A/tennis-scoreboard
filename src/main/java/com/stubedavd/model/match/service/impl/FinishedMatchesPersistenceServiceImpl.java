package com.stubedavd.model.match.service.impl;

import com.stubedavd.model.match.entity.Match;
import com.stubedavd.mapper.match.MatchMapper;
import com.stubedavd.model.match.domain.MatchScoreModel;
import com.stubedavd.model.match.repository.MatchRepository;
import com.stubedavd.model.match.service.FinishedMatchesPersistenceService;
import com.stubedavd.mapper.player.PlayerMapper;
import com.stubedavd.model.player.entity.Player;
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
