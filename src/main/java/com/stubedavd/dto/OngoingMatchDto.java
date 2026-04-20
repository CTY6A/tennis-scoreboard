package com.stubedavd.dto;

import com.stubedavd.entity.Player;
import com.stubedavd.model.MatchScoreModel;

public record OngoingMatchDto(Player player1, Player player2, MatchScoreModel matchScoreModel) {}
