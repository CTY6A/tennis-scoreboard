package com.stubedavd.service;

import com.stubedavd.dto.MatchDto;
import com.stubedavd.dto.PlayerDto;
import java.util.UUID;

public interface OngoingMatchService {

    MatchDto create(PlayerDto playerDto1, PlayerDto playerDto2);

    MatchDto get(UUID uuid);

    void remove(UUID uuid);
}
