package com.stubedavd.player.mapper;

import com.stubedavd.player.dto.request.PlayerRequestDto;
import com.stubedavd.player.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerRequestDto toRequestDto(String name);

    @Mapping(target = "id", ignore = true)
    Player toModel(PlayerRequestDto playerRequestDto);
}
