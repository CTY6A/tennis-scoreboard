package com.stubedavd.mapper;

import com.stubedavd.dto.response.MatchResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchMapper {

    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    MatchResponseDto toResponseDto(String player1Name, String player2Name, String winnerName);
}
