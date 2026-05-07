package com.stubedavd.match.mapper;

import com.stubedavd.match.model.domain.MatchScoreModel;
import com.stubedavd.player.model.domain.PlayerDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchScoreModelMapper {

    MatchScoreModelMapper INSTANCE = Mappers.getMapper(MatchScoreModelMapper.class);

    @Mapping(target = "winner", ignore = true)
    @Mapping(target = "points", ignore = true)
    @Mapping(target = "games", ignore = true)
    @Mapping(target = "sets", ignore = true)
    @Mapping(target = "matchFinished", ignore = true)
    @Mapping(target = "score", ignore = true)
    MatchScoreModel toDomain(PlayerDomain player1, PlayerDomain player2);
}
