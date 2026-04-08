package com.stubedavd.mapper;

import com.stubedavd.dto.request.PlayerRequestDto;
import com.stubedavd.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerRequestDto toPlayerRequestDto(String name);

    @Mapping(target = "id", ignore = true)
    Player toModel(PlayerRequestDto playerRequestDto);
}
