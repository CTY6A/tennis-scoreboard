package com.stubedavd.mapper;

import com.stubedavd.dto.response.PlayerScoreResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerScoreMapper {

    PlayerScoreMapper INSTANCE = Mappers.getMapper(PlayerScoreMapper.class);

    PlayerScoreResponseDto toResponseDto(String name, Integer points, Integer games, Integer sets);
}
