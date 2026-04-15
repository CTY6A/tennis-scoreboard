package com.stubedavd.service.impl;

import com.stubedavd.dto.OngoingMatchDto;
import com.stubedavd.dto.response.MatchScoreResponseDto;
import com.stubedavd.dto.response.PlayerScoreResponseDto;
import com.stubedavd.entity.Player;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.mapper.MatchScoreMapper;
import com.stubedavd.mapper.PlayerScoreMapper;
import com.stubedavd.model.MatchScoreModel;
import com.stubedavd.service.MatchScoreCalculationService;
import com.stubedavd.service.MatchScoreService;
import com.stubedavd.service.OngoingMatchService;

import java.util.UUID;

public class MatchScoreServiceImpl implements MatchScoreService {

    OngoingMatchService ongoingMatchService;
    MatchScoreCalculationService matchScoreCalculationService;

    MatchScoreMapper matchScoreMapper;
    PlayerScoreMapper playerScoreMapper;

    public MatchScoreServiceImpl(
            OngoingMatchService ongoingMatchService,
            MatchScoreCalculationService matchScoreCalculationService,
            MatchScoreMapper matchScoreMapper,
            PlayerScoreMapper playerScoreMapper
    ) {

        this.ongoingMatchService = ongoingMatchService;
        this.matchScoreCalculationService = matchScoreCalculationService;
        this.matchScoreMapper = matchScoreMapper;
        this.playerScoreMapper = playerScoreMapper;
    }

    @Override
    public MatchScoreResponseDto getMatchScore(UUID uuid) {

        OngoingMatchDto ongoingMatchDto = ongoingMatchService.get(uuid);

        MatchScoreModel matchScoreModel = ongoingMatchDto.matchScoreModel();

        String player1PointsString;
        String player2PointsString;

        Integer player1Points = matchScoreModel.getPoints().get(ongoingMatchDto.player1());
        Integer player2Points = matchScoreModel.getPoints().get(ongoingMatchDto.player2());

        if (player1Points >= 4 || player2Points >= 4) {

            player1PointsString = "40";
            player2PointsString = "40";

            if (player1Points > player2Points) {

                player1PointsString = "AD";
            } else if (player2Points > player1Points) {

                player2PointsString = "AD";
            }
        } else {

            player1PointsString = switch (player1Points) {
                case 1 -> "15";
                case 2 -> "30";
                case 3 -> "40";
                default -> "0";
            };

            player2PointsString = switch (player2Points) {
                case 1 -> "15";
                case 2 -> "30";
                case 3 -> "40";
                default -> "0";
            };
        }

        PlayerScoreResponseDto playerScoreResponseDto1 = playerScoreMapper.toResponseDto(
                ongoingMatchDto.player1().getId(),
                ongoingMatchDto.player1().getName(),
                matchScoreModel.getSets().get(ongoingMatchDto.player1()),
                matchScoreModel.getGames().get(ongoingMatchDto.player1()),
                player1PointsString
        );

        PlayerScoreResponseDto playerScoreResponseDto2 = playerScoreMapper.toResponseDto(
                ongoingMatchDto.player2().getId(),
                ongoingMatchDto.player2().getName(),
                matchScoreModel.getSets().get(ongoingMatchDto.player2()),
                matchScoreModel.getGames().get(ongoingMatchDto.player2()),
                player2PointsString
        );

        return matchScoreMapper.toResponseDto(
                playerScoreResponseDto1,
                playerScoreResponseDto2
        );
    }

    @Override
    public void playerScore(UUID uuid, Integer playerId) {

        OngoingMatchDto ongoingMatchDto = ongoingMatchService.get(uuid);
        MatchScoreModel matchScoreModel = ongoingMatchDto.matchScoreModel();

        Player player;
        if (playerId.equals(ongoingMatchDto.player1().getId())) {

            player = ongoingMatchDto.player1();
        } else if (playerId.equals(ongoingMatchDto.player2().getId())) {

            player = ongoingMatchDto.player2();
        } else {

            throw new NotFoundException("Player with this id does not exist in this match score");
        }

        matchScoreCalculationService.pointWon(matchScoreModel, player);


    }

    @Override
    public boolean isMatchFinished(UUID uuid) {

        OngoingMatchDto ongoingMatchDto = ongoingMatchService.get(uuid);
        MatchScoreModel matchScoreModel = ongoingMatchDto.matchScoreModel();

        return matchScoreCalculationService.isMatchFinished(matchScoreModel);
    }
}
