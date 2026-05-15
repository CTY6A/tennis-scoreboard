package com.stubedavd.match.model.service.impl;

import com.stubedavd.match.model.domain.MatchScoreModel;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.match.model.service.OngoingMatchService;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchServiceImpl implements OngoingMatchService {
    private static final String MATCH_NOT_FOUND = "Match not found";

    private final Map<UUID, MatchScoreModel> ongoingMatches;

    public OngoingMatchServiceImpl() {
        this.ongoingMatches = new ConcurrentHashMap<>();
    }

    public UUID save(MatchScoreModel matchScoreModel) {
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
