package com.stubedavd.service;

import com.stubedavd.dto.OngoingMatchDto;
import com.stubedavd.entity.Player;

import java.util.UUID;

public interface OngoingMatchService extends CrudService {

    UUID save(Player player1, Player player2);

    OngoingMatchDto get(UUID uuid);
}
