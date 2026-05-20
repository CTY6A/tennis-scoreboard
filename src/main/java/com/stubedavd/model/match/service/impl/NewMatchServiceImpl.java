package com.stubedavd.model.match.service.impl;

import com.stubedavd.mapper.player.PlayerMapper;
import com.stubedavd.model.match.dto.request.MatchRequestDto;
import com.stubedavd.model.match.service.NewMatchService;
import com.stubedavd.model.match.service.OngoingMatchService;
import com.stubedavd.model.player.domain.PlayerDomain;
import com.stubedavd.model.player.entity.Player;
import com.stubedavd.model.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class NewMatchServiceImpl implements NewMatchService {
    private final OngoingMatchService ongoingMatchService;
    private final PlayerMapper playerMapper;
    private final PlayerRepository playerRepository;

    @Override
    public UUID create(MatchRequestDto matchRequestDto) {
        Player player1 = playerRepository.findByName(matchRequestDto.player1Name())
                .orElseGet(() -> playerRepository.save(playerMapper.toEntity(matchRequestDto.player1Name())));
        Player player2 = playerRepository.findByName(matchRequestDto.player2Name())
                .orElseGet(() -> playerRepository.save(playerMapper.toEntity(matchRequestDto.player2Name())));

        PlayerDomain player1Domain = playerMapper.toDomain(player1);
        PlayerDomain player2Domain = playerMapper.toDomain(player2);

        return ongoingMatchService.save(player1Domain, player2Domain);
    }
}
