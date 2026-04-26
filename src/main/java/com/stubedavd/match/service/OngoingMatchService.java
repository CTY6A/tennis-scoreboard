package com.stubedavd.match.service;

import com.stubedavd.match.model.MatchScoreModel;
import com.stubedavd.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OngoingMatchService {

    private final Map<UUID, MatchScoreModel> ongoingMatches;

    public OngoingMatchService() {

        this.ongoingMatches = new HashMap<>();
    }

    public UUID save(MatchScoreModel matchScoreModel) {

        UUID uuid = UUID.randomUUID();

        ongoingMatches.put(uuid, matchScoreModel);

        return uuid;
    }

    public MatchScoreModel get(UUID uuid) {

        MatchScoreModel matchScoreModel = ongoingMatches.get(uuid);

        if (matchScoreModel == null) {

            throw new NotFoundException("Match not found");
        }

        return matchScoreModel;
    }

    public void delete(UUID uuid) {

        ongoingMatches.remove(uuid);
    }
}
