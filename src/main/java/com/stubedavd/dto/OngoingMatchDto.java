package com.stubedavd.dto;

import com.stubedavd.entity.Player;
import com.stubedavd.model.MatchScoreModel;

import java.util.UUID;

public record OngoingMatchDto(Player player1, Player player2, MatchScoreModel matchScoreModel) {}
