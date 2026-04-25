package com.stubedavd.match.dto;

import com.stubedavd.player.entity.Player;
import com.stubedavd.match.model.MatchScoreModel;

public record OngoingMatchDto(Player player1, Player player2, MatchScoreModel matchScoreModel) {}
