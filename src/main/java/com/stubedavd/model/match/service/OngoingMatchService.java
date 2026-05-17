package com.stubedavd.model.match.service;

import com.stubedavd.model.match.domain.MatchScoreModel;
import com.stubedavd.model.player.domain.PlayerDomain;

import java.util.UUID;

public interface OngoingMatchService {
    UUID save(PlayerDomain player1Domain, PlayerDomain player2Domain);
    MatchScoreModel get(UUID uuid);
    void delete(UUID uuid);
}
