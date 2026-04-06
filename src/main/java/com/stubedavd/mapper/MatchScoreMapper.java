package com.stubedavd.mapper;

import com.stubedavd.dto.PlayerDto;
import com.stubedavd.model.MatchScoreModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchScoreMapper {

    MatchScoreMapper INSTANCE = Mappers.getMapper(MatchScoreMapper.class);

    @Mapping(target = "winner", ignore = true)
    @Mapping(target = "points", ignore = true)
    @Mapping(target = "games", ignore = true)
    @Mapping(target = "sets", ignore = true)
    @Mapping(target = "tieBreak", ignore = true)
    @Mapping(target = "matchFinished", ignore = true)
    MatchScoreModel toModel(PlayerDto playerDto1, PlayerDto playerDto2);
}
