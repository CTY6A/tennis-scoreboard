package com.stubedavd.util;

import com.stubedavd.dto.response.MatchResponseDto;
import com.stubedavd.dto.request.MatchRequestDto;
import com.stubedavd.dto.request.PlayerRequestDto;
import com.stubedavd.mapper.MatchMapper;
import com.stubedavd.mapper.MatchScoreMapper;
import com.stubedavd.mapper.PlayerMapper;
import com.stubedavd.service.OngoingMatchServiceImpl;

//TODO: delete this class
public class Main {

    public static void main(String[] args) {

        PlayerMapper playerMapper = PlayerMapper.INSTANCE;
        MatchMapper matchMapper = MatchMapper.INSTANCE;
        MatchScoreMapper matchScoreMapper = MatchScoreMapper.INSTANCE;

        OngoingMatchServiceImpl.init(matchMapper, matchScoreMapper, playerMapper);
        OngoingMatchServiceImpl ongoingMatchService = OngoingMatchServiceImpl.getInstance();

        PlayerRequestDto playerRequestDto1 = playerMapper.toPlayerRequestDto("Nadal");
        PlayerRequestDto playerRequestDto2 = playerMapper.toPlayerRequestDto("Federer");

        MatchRequestDto matchRequestDto = matchMapper.toMatchRequestDto(playerRequestDto1, playerRequestDto2);

        MatchResponseDto matchResponseDto = ongoingMatchService.create(matchRequestDto);

        System.out.println(matchResponseDto);
    }
}
