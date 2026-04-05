package com.stubedavd.service;

import com.stubedavd.dto.MatchDto;
import com.stubedavd.dto.PlayerDto;
import com.stubedavd.dto.MatchScoreDto;
import com.stubedavd.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OngoingMatchServiceImpl implements OngoingMatchService {

    private static OngoingMatchServiceImpl instance;

    private final Map<UUID, MatchDto> ongoingMatches;

    private OngoingMatchServiceImpl() {

        this.ongoingMatches = new HashMap<>();
    }

    public synchronized OngoingMatchServiceImpl getInstance() {

        if (instance == null) {
            instance = new OngoingMatchServiceImpl();
        }

        return instance;
    }

    @Override
    public MatchDto create(PlayerDto playerDto1, PlayerDto playerDto2) {

        UUID uuid = UUID.randomUUID();

        MatchScoreDto matchScoreDto = new MatchScoreDto();

        MatchDto matchDto = new MatchDto(uuid, playerDto1, playerDto2, matchScoreDto);

        ongoingMatches.put(uuid, matchDto);

        return matchDto;
    }

    @Override
    public MatchDto get(UUID uuid) {

        MatchDto matchDto = ongoingMatches.get(uuid);

        if (matchDto == null) {

            throw new NotFoundException("Match not found");
        }

        return matchDto;
    }

    @Override
    public void remove(UUID uuid) {

        ongoingMatches.remove(uuid);
    }
}
