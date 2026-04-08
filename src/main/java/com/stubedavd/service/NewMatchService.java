package com.stubedavd.service;

import com.stubedavd.dto.request.PlayerRequestDto;

import java.util.UUID;

public interface NewMatchService {

    UUID newMatch(PlayerRequestDto playerRequestDto1, PlayerRequestDto playerRequestDto2);
}
