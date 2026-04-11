package com.stubedavd.service.impl;

import com.stubedavd.dto.OngoingMatchDto;
import com.stubedavd.dto.response.MatchScoreResponseDto;
import com.stubedavd.dto.response.PlayerScoreResponseDto;
import com.stubedavd.mapper.MatchScoreMapper;
import com.stubedavd.mapper.PlayerMapper;
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

        PlayerScoreResponseDto playerScoreResponseDto1 = playerScoreMapper.toResponseDto(
                ongoingMatchDto.player1().getName(),
                matchScoreModel.getPoints().get(ongoingMatchDto.player1()),
                matchScoreModel.getGames().get(ongoingMatchDto.player1()),
                matchScoreModel.getSets().get(ongoingMatchDto.player1())
        );
        return null;
    }
}
