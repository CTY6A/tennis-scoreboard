package com.stubedavd.match.service;

import com.stubedavd.match.mapper.MatchScoreModelMapper;
import com.stubedavd.match.model.MatchScoreModel;
import com.stubedavd.player.dto.request.PlayerRequestDto;
import com.stubedavd.player.entity.Player;
import com.stubedavd.player.mapper.PlayerMapper;
import com.stubedavd.player.repository.PlayerRepository;

import java.util.UUID;

public class NewMatchService {

    private final OngoingMatchService ongoingMatchService;

    private final PlayerMapper playerMapper;
    private final MatchScoreModelMapper matchScoreModelMapper;

    private final PlayerRepository playerRepository;

    public NewMatchService(
            OngoingMatchService ongoingMatchService,
            PlayerMapper playerMapper,
            MatchScoreModelMapper matchScoreModelMapper,
            PlayerRepository playerRepository
    ) {

        this.ongoingMatchService = ongoingMatchService;
        this.playerMapper = playerMapper;
        this.matchScoreModelMapper = matchScoreModelMapper;
        this.playerRepository = playerRepository;
    }

    public UUID newMatch(PlayerRequestDto playerRequestDto1, PlayerRequestDto playerRequestDto2) {

        Player player1 = playerRepository.findByName(playerRequestDto1.name())
                .orElseGet(() -> playerRepository.save(playerMapper.toModel(playerRequestDto1)));

        Player player2 = playerRepository.findByName(playerRequestDto2.name())
                .orElseGet(() -> playerRepository.save(playerMapper.toModel(playerRequestDto2)));

        MatchScoreModel matchScoreModel = matchScoreModelMapper.toModel(player1, player2);

        return ongoingMatchService.save(matchScoreModel);
    }
}
