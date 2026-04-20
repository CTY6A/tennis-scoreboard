package com.stubedavd.mapper;

import com.stubedavd.dto.response.MatchResponseDto;
import com.stubedavd.entity.Match;
import com.stubedavd.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchMapper {

    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    MatchResponseDto toResponseDto(String player1Name, String player2Name, String winnerName);

    @Mapping(target = "id", ignore = true)
    Match toModel(Player player1, Player player2, Player winner);
}
