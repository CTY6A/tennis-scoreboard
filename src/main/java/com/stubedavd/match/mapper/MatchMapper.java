package com.stubedavd.match.mapper;

import com.stubedavd.match.model.dto.response.MatchResponseDto;
import com.stubedavd.match.model.entity.Match;
import com.stubedavd.player.model.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchMapper {

    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    MatchResponseDto toResponseDto(String player1Name, String player2Name, String winnerName);

    @Mapping(target = "id", ignore = true)
    Match toEntity(Player player1, Player player2, Player winner);
}
