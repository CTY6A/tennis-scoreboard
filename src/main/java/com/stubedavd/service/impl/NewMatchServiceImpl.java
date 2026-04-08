package com.stubedavd.service.impl;

import com.stubedavd.dto.OngoingMatchDto;
import com.stubedavd.dto.request.PlayerRequestDto;
import com.stubedavd.entity.Player;
import com.stubedavd.mapper.PlayerMapper;
import com.stubedavd.repository.PlayerRepository;
import com.stubedavd.service.NewMatchService;
import com.stubedavd.service.OngoingMatchService;

import java.util.UUID;

public class NewMatchServiceImpl implements NewMatchService {

    PlayerRepository playerRepository;

    OngoingMatchService ongoingMatchService;

    PlayerMapper playerMapper;

    public NewMatchServiceImpl(
            PlayerRepository playerRepository,
            OngoingMatchService ongoingMatchService,
            PlayerMapper playerMapper
    ) {

        this.playerRepository = playerRepository;
        this.ongoingMatchService = ongoingMatchService;
        this.playerMapper = playerMapper;
    }

    @Override
    public UUID newMatch(PlayerRequestDto playerRequestDto1, PlayerRequestDto playerRequestDto2) {

        Player player1 = playerRepository.findByName(playerRequestDto1.name())
                .orElse(playerRepository.save(playerMapper.toModel(playerRequestDto1)));

        Player player2 = playerRepository.findByName(playerRequestDto2.name())
                .orElse(playerRepository.save(playerMapper.toModel(playerRequestDto2)));

        return ongoingMatchService.save(player1, player2);
    }
}
