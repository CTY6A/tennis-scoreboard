package com.stubedavd.model.match.service.impl;

import com.stubedavd.mapper.match.MatchScoreModelMapper;
import com.stubedavd.model.match.domain.MatchScoreModel;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.model.match.service.OngoingMatchService;
import com.stubedavd.model.player.domain.PlayerDomain;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchServiceImpl implements OngoingMatchService {
    private static final String MATCH_NOT_FOUND = "Match not found";

    private final Map<UUID, MatchScoreModel> ongoingMatches;
    private final MatchScoreModelMapper matchScoreModelMapper;

    public OngoingMatchServiceImpl(MatchScoreModelMapper matchScoreModelMapper) {
        this.ongoingMatches = new ConcurrentHashMap<>();
        this.matchScoreModelMapper = matchScoreModelMapper;
    }

    public UUID save(PlayerDomain player1Domain, PlayerDomain player2Domain) {
        MatchScoreModel matchScoreModel = matchScoreModelMapper.toDomain(player1Domain, player2Domain);
        UUID uuid = UUID.randomUUID();
        ongoingMatches.put(uuid, matchScoreModel);
        return uuid;
    }

    public MatchScoreModel get(UUID uuid) {
        MatchScoreModel matchScoreModel = ongoingMatches.get(uuid);
        if (matchScoreModel == null) {
            throw new NotFoundException(MATCH_NOT_FOUND);
        }
        return matchScoreModel;
    }

    public void delete(UUID uuid) {
        ongoingMatches.remove(uuid);
    }
}
