package com.stubedavd.mapper;

import com.stubedavd.dto.PlayerDto;
import com.stubedavd.dto.response.PlayerResponseDto;
import com.stubedavd.dto.request.PlayerRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerRequestDto toPlayerRequestDto(String name);

    PlayerDto toPlayerDto(PlayerRequestDto playerRequestDto);

    PlayerResponseDto toPlayerResponseDto(Integer id, PlayerDto playerDto);
}
