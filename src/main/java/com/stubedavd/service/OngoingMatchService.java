package com.stubedavd.service;

import com.stubedavd.dto.MatchDto;
import com.stubedavd.dto.MatchScoreDto;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.model.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class OngoingMatchService {

    private static final OngoingMatchService INSTANCE = new OngoingMatchService();

    private final Map<UUID, MatchDto> ongoingMatches = new HashMap<>();

    private OngoingMatchService() {}

    public OngoingMatchService getInstance() {
        return INSTANCE;
    }

    UUID createMatch(Player player1, Player player2) {

        UUID uuid = UUID.randomUUID();
        MatchScoreDto matchScoreDto = new MatchScoreDto();
        MatchDto matchDto = new MatchDto(uuid, player1, player2, matchScoreDto);
        ongoingMatches.put(uuid, matchDto);
        return uuid;
    }

    MatchDto getMatch(UUID uuid) {

        MatchDto matchDto = ongoingMatches.get(uuid);

        if (matchDto == null) {
            throw new NotFoundException("Match not found");
        }

        return matchDto;
    }

    void removeMatch(UUID uuid) {
        ongoingMatches.remove(uuid);
    }
}
