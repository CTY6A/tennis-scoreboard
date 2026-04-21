package com.stubedavd.service;

import com.stubedavd.dto.request.PlayerRequestDto;
import com.stubedavd.entity.Player;
import com.stubedavd.mapper.PlayerMapper;
import com.stubedavd.repository.PlayerRepository;

import java.util.UUID;

public class NewMatchService {

    PlayerRepository playerRepository;

    OngoingMatchService ongoingMatchService;

    PlayerMapper playerMapper;

    public NewMatchService(
            PlayerRepository playerRepository,
            OngoingMatchService ongoingMatchService,
            PlayerMapper playerMapper
    ) {

        this.playerRepository = playerRepository;
        this.ongoingMatchService = ongoingMatchService;
        this.playerMapper = playerMapper;
    }

    public UUID newMatch(PlayerRequestDto playerRequestDto1, PlayerRequestDto playerRequestDto2) {

        Player player1 = playerRepository.findByName(playerRequestDto1.name())
                .orElseGet(() -> playerRepository.save(playerMapper.toModel(playerRequestDto1)));

        Player player2 = playerRepository.findByName(playerRequestDto2.name())
                .orElseGet(() -> playerRepository.save(playerMapper.toModel(playerRequestDto2)));

        return ongoingMatchService.save(player1, player2);
    }
}
