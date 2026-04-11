package com.stubedavd.mapper;

import com.stubedavd.dto.response.MatchScoreResponseDto;
import com.stubedavd.dto.response.PlayerScoreResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchScoreMapper {

    MatchScoreMapper INSTANCE = Mappers.getMapper(MatchScoreMapper.class);

    MatchScoreResponseDto toResponseDto(
            PlayerScoreResponseDto playerScoreResponseDto1,
            PlayerScoreResponseDto playerScoreResponseDto2
    );
}
