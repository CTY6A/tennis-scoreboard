package com.stubedavd.service;

import com.stubedavd.dto.response.MatchResponseDto;
import com.stubedavd.dto.request.MatchRequestDto;

import java.util.UUID;

public interface OngoingMatchService extends CrudService<MatchRequestDto, MatchResponseDto> {

    MatchResponseDto get(UUID uuid);
}
