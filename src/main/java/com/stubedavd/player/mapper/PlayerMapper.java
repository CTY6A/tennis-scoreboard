package com.stubedavd.player.mapper;

import com.stubedavd.player.model.domain.PlayerDomain;
import com.stubedavd.player.model.dto.request.PlayerRequestDto;
import com.stubedavd.player.model.dto.response.PlayerResponseDto;
import com.stubedavd.player.model.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerRequestDto toRequestDto(String name);

    @Mapping(target = "id", ignore = true)
    Player toEntity(PlayerRequestDto playerRequestDto);

    Player toEntity(PlayerDomain playerDomain);

    PlayerDomain toDomain(Player player);

    PlayerResponseDto toResponseDto(Player player);

    @ObjectFactory
    default Player createPlayerFromRequestDto(PlayerRequestDto dto) {
        return new Player(dto.name());
    }

    @ObjectFactory
    default Player createPlayerFromDomain(PlayerDomain domain) {
        return new Player(domain.id(), domain.name());
    }
}
