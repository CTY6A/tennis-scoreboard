package com.stubedavd.match.model.service;

import com.stubedavd.match.model.domain.MatchScoreModel;
import com.stubedavd.player.model.domain.PlayerDomain;

import java.util.UUID;

public interface OngoingMatchService {
    UUID save(PlayerDomain player1Domain, PlayerDomain player2Domain);
    MatchScoreModel get(UUID uuid);
    void delete(UUID uuid);
}
