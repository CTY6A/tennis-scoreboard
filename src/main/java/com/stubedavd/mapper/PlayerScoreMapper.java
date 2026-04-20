package com.stubedavd.mapper;

import com.stubedavd.dto.response.PlayerFinalScoreResponseDto;
import com.stubedavd.dto.response.PlayerScoreResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PlayerScoreMapper {

    PlayerScoreMapper INSTANCE = Mappers.getMapper(PlayerScoreMapper.class);

    PlayerScoreResponseDto toResponseDto(Integer id, String name, Integer sets, Integer games, String points);

    PlayerFinalScoreResponseDto toFinalScoreResponseDto(String name, List<Integer> score);
}
