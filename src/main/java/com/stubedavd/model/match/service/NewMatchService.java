package com.stubedavd.model.match.service;

import com.stubedavd.model.match.dto.request.MatchRequestDto;

import java.util.UUID;

public interface NewMatchService {
    UUID create(MatchRequestDto matchRequestDto);
}
