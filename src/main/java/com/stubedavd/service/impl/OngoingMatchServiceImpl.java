package com.stubedavd.service.impl;

import com.stubedavd.dto.MatchDto;
import com.stubedavd.dto.PlayerDto;
import com.stubedavd.dto.response.MatchResponseDto;
import com.stubedavd.dto.response.PlayerResponseDto;
import com.stubedavd.mapper.MatchScoreMapper;
import com.stubedavd.mapper.PlayerMapper;
import com.stubedavd.model.MatchScoreModel;
import com.stubedavd.dto.request.MatchRequestDto;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.mapper.MatchMapper;
import com.stubedavd.service.OngoingMatchService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OngoingMatchServiceImpl implements OngoingMatchService {

    private final Map<UUID, MatchResponseDto> ongoingMatches;
    private final MatchMapper matchMapper;
    private final MatchScoreMapper matchScoreMapper;
    private final PlayerMapper playerMapper;

    public OngoingMatchServiceImpl(
            MatchMapper matchMapper,
            MatchScoreMapper matchScoreMapper,
            PlayerMapper playerMapper
    ) {

        this.matchMapper = matchMapper;
        this.matchScoreMapper = matchScoreMapper;
        this.playerMapper = playerMapper;

        this.ongoingMatches = new HashMap<>();
    }

    @Override
    public MatchResponseDto create(MatchRequestDto matchRequestDto) {

        UUID uuid = UUID.randomUUID();

        PlayerDto playerDto1 = playerMapper.toPlayerDto(matchRequestDto.playerRequestDto1());
        PlayerDto playerDto2 = playerMapper.toPlayerDto(matchRequestDto.playerRequestDto2());

        MatchDto matchDto = matchMapper.toMatchDto(playerDto1, playerDto2);

        MatchScoreModel matchScoreModel = matchScoreMapper.toModel(matchDto.playerDto1(), matchDto.playerDto2());

        //TODO: delete constants
        PlayerResponseDto playerResponseDto1 = playerMapper.toPlayerResponseDto(0, playerDto1);
        PlayerResponseDto playerResponseDto2 = playerMapper.toPlayerResponseDto(0, playerDto2);

        MatchResponseDto matchResponseDto = matchMapper.toMatchResponseDto(
                uuid,
                playerResponseDto1,
                playerResponseDto2,
                matchScoreModel
        );

        ongoingMatches.put(uuid, matchResponseDto);

        return matchResponseDto;
    }

    @Override
    public MatchResponseDto get(UUID uuid) {

        MatchResponseDto matchResponseDto = ongoingMatches.get(uuid);

        if (matchResponseDto == null) {

            throw new NotFoundException("Match not found");
        }

        return matchResponseDto;
    }

    @Override
    public void delete(UUID uuid) {

        ongoingMatches.remove(uuid);
    }
}
