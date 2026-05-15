package com.stubedavd.match.model.service;

import com.stubedavd.match.model.domain.MatchScoreModel;

import java.util.UUID;

public interface OngoingMatchService {
    UUID save(MatchScoreModel matchScoreModel);
    MatchScoreModel get(UUID uuid);
    void delete(UUID uuid);
}
