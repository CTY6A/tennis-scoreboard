package com.stubedavd.model.match.service.impl;

import com.stubedavd.exception.NotFoundException;
import com.stubedavd.mapper.match.MatchMapper;
import com.stubedavd.mapper.match.MatchScoreModelMapper;
import com.stubedavd.mapper.player.PlayerMapper;
import com.stubedavd.model.match.domain.MatchScoreModel;
import com.stubedavd.model.match.dto.response.FinalScoreResponseDto;
import com.stubedavd.model.match.dto.response.MatchScoreResponseDto;
import com.stubedavd.model.match.entity.Match;
import com.stubedavd.model.match.repository.MatchRepository;
import com.stubedavd.model.match.service.MatchScoreCalculationService;
import com.stubedavd.model.match.service.MatchScoreService;
import com.stubedavd.model.match.service.OngoingMatchService;
import com.stubedavd.model.player.domain.PlayerDomain;
import com.stubedavd.model.player.entity.Player;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class MatchScoreServiceImpl implements MatchScoreService {
    private static final String PLAYER_NOT_EXIST =
            "Player with this id does not exist in this match score";
    private final OngoingMatchService ongoingMatchService;
    private final MatchScoreCalculationService matchScoreCalculationService;
    private final PlayerMapper playerMapper;
    private final MatchMapper matchMapper;
    private final MatchScoreModelMapper matchScoreModelMapper;
    private final MatchRepository matchRepository;

    @Override
    public MatchScoreResponseDto getScore(UUID uuid) {
        MatchScoreModel matchScoreModel = ongoingMatchService.get(uuid);
        return matchScoreModelMapper.toResponseDto(matchScoreModel);
    }

    @Override
    public boolean isMatchFinished(UUID uuid) {
        MatchScoreModel matchScoreModel = ongoingMatchService.get(uuid);
        return matchScoreCalculationService.isMatchFinished(matchScoreModel);
    }

    @Override
    public void pointWon(UUID uuid, Long playerId) {
        MatchScoreModel matchScoreModel = ongoingMatchService.get(uuid);

        PlayerDomain player;

        if (playerId.equals(matchScoreModel.getPlayer1().id())) {
            player = matchScoreModel.getPlayer1();
        } else if (playerId.equals(matchScoreModel.getPlayer2().id())) {
            player = matchScoreModel.getPlayer2();
        } else {
            throw new NotFoundException(PLAYER_NOT_EXIST);
        }

        matchScoreCalculationService.pointWon(matchScoreModel, player);
    }

    @Override
    public FinalScoreResponseDto recordMatch(UUID uuid) {
        MatchScoreModel matchScoreModel = ongoingMatchService.get(uuid);

        Player player1 = playerMapper.toEntity(matchScoreModel.getPlayer1());
        Player player2 = playerMapper.toEntity(matchScoreModel.getPlayer2());
        Player winner = playerMapper.toEntity(matchScoreModel.getWinner());

        Match match = matchMapper.toEntity(
                player1,
                player2,
                winner
        );

        matchRepository.save(match);
        ongoingMatchService.delete(uuid);

        return matchScoreModelMapper.toFinalResponseDto(matchScoreModel);
    }
}
