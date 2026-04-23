package com.stubedavd.service;

import com.stubedavd.dto.OngoingMatchDto;
import com.stubedavd.dto.response.FinalScoreResponseDto;
import com.stubedavd.dto.response.MatchScoreResponseDto;
import com.stubedavd.dto.response.PlayerScoreResponseDto;
import com.stubedavd.entity.Match;
import com.stubedavd.entity.Player;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.mapper.MatchMapper;
import com.stubedavd.mapper.MatchScoreMapper;
import com.stubedavd.mapper.PlayerScoreMapper;
import com.stubedavd.model.MatchScoreModel;
import com.stubedavd.repository.MatchRepository;

import java.util.UUID;

public class MatchScoreService {

    OngoingMatchService ongoingMatchService;
    MatchScoreCalculationService matchScoreCalculationService;

    MatchMapper matchMapper;
    MatchScoreMapper matchScoreMapper;
    PlayerScoreMapper playerScoreMapper;

    MatchRepository matchRepository;

    public MatchScoreService(
            OngoingMatchService ongoingMatchService,
            MatchScoreCalculationService matchScoreCalculationService,
            MatchMapper matchMapper,
            MatchScoreMapper matchScoreMapper,
            PlayerScoreMapper playerScoreMapper,
            MatchRepository matchRepository
    ) {

        this.ongoingMatchService = ongoingMatchService;
        this.matchScoreCalculationService = matchScoreCalculationService;
        this.matchMapper = matchMapper;
        this.matchScoreMapper = matchScoreMapper;
        this.playerScoreMapper = playerScoreMapper;
        this.matchRepository = matchRepository;
    }

    public MatchScoreResponseDto getMatchScore(UUID uuid) {

        OngoingMatchDto ongoingMatchDto = ongoingMatchService.get(uuid);

        MatchScoreModel matchScoreModel = ongoingMatchDto.matchScoreModel();

        String player1PointsString;
        String player2PointsString;

        int player1Points = matchScoreModel.getPoints().getScore(ongoingMatchDto.player1());
        int player2Points = matchScoreModel.getPoints().getScore(ongoingMatchDto.player2());

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
                matchScoreModel.getSets().getScore(ongoingMatchDto.player1()),
                matchScoreModel.getGames().getScore(ongoingMatchDto.player1()),
                player1PointsString
        );

        PlayerScoreResponseDto playerScoreResponseDto2 = playerScoreMapper.toResponseDto(
                ongoingMatchDto.player2().getId(),
                ongoingMatchDto.player2().getName(),
                matchScoreModel.getSets().getScore(ongoingMatchDto.player2()),
                matchScoreModel.getGames().getScore(ongoingMatchDto.player2()),
                player2PointsString
        );

        return matchScoreMapper.toResponseDto(
                playerScoreResponseDto1,
                playerScoreResponseDto2
        );
    }

    public FinalScoreResponseDto getFinalScore(UUID uuid) {

        OngoingMatchDto ongoingMatchDto = ongoingMatchService.get(uuid);

        MatchScoreModel matchScoreModel = ongoingMatchDto.matchScoreModel();

        return matchScoreMapper.toFinalScoreResponseDto(
                playerScoreMapper.toFinalScoreResponseDto(
                        ongoingMatchDto.player1().getName(),
                        matchScoreModel
                                .getScore()
                                .stream()
                                .map(matchGame -> matchGame.getScore(ongoingMatchDto.player1()))
                                .toList()
                ),
                playerScoreMapper.toFinalScoreResponseDto(
                        ongoingMatchDto.player2().getName(),
                        matchScoreModel.getScore()
                                .stream()
                                .map(matchGame -> matchGame.getScore(ongoingMatchDto.player2()))
                                .toList()
                )
        );
    }

    public void recordMatch(UUID uuid) {

        OngoingMatchDto ongoingMatchDto = ongoingMatchService.get(uuid);

        Match match = matchMapper.toModel(
                ongoingMatchDto.player1(),
                ongoingMatchDto.player2(),
                ongoingMatchDto.matchScoreModel().getWinner()
        );

        matchRepository.save(match);

        ongoingMatchService.delete(uuid);
    }

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

    public boolean isMatchFinished(UUID uuid) {

        OngoingMatchDto ongoingMatchDto = ongoingMatchService.get(uuid);
        MatchScoreModel matchScoreModel = ongoingMatchDto.matchScoreModel();

        return matchScoreCalculationService.isMatchFinished(matchScoreModel);
    }
}
