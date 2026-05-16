package com.stubedavd.player.mapper;

import com.stubedavd.player.model.domain.PlayerDomain;
import com.stubedavd.player.model.dto.response.PlayerResponseDto;
import com.stubedavd.player.model.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    @Mapping(target = "id", ignore = true)
    Player toEntity(String playerName);

    Player toEntity(PlayerDomain playerDomain);

    PlayerDomain toDomain(Player player);

    PlayerResponseDto toResponseDto(Player player);

    @ObjectFactory
    default Player createPlayerFromRequestDto(String name) {
        return new Player(name);
    }

    @ObjectFactory
    default Player createPlayerFromDomain(PlayerDomain domain) {
        return new Player(domain.id(), domain.name());
    }
}
