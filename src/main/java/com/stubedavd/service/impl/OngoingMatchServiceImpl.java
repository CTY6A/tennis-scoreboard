package com.stubedavd.service.impl;

import com.stubedavd.dto.OngoingMatchDto;
import com.stubedavd.entity.Player;
import com.stubedavd.model.MatchScoreModel;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.service.OngoingMatchService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OngoingMatchServiceImpl implements OngoingMatchService {

    private final Map<UUID, OngoingMatchDto> ongoingMatches;

    public OngoingMatchServiceImpl() {

        this.ongoingMatches = new HashMap<>();
    }

    @Override
    public UUID save(Player player1, Player player2) {

        UUID uuid = UUID.randomUUID();

        MatchScoreModel matchScoreModel = new MatchScoreModel(player1, player2);

        OngoingMatchDto ongoingMatchDto = new OngoingMatchDto(player1, player2, matchScoreModel);

        ongoingMatches.put(uuid, ongoingMatchDto);

        return uuid;
    }

    @Override
    public OngoingMatchDto get(UUID uuid) {

        OngoingMatchDto ongoingMatchDto = ongoingMatches.get(uuid);

        if (ongoingMatchDto == null) {

            throw new NotFoundException("Match not found");
        }

        return ongoingMatchDto;
    }

    @Override
    public void delete(UUID uuid) {

        ongoingMatches.remove(uuid);
    }
}
