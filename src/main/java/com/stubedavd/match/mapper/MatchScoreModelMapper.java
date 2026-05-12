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
    @Mapping(target = "regularGameScore", ignore = true)
    @Mapping(target = "tiebreakScore", ignore = true)
    @Mapping(target = "setScore", ignore = true)
    @Mapping(target = "matchScore", ignore = true)
    @Mapping(target = "protocol", ignore = true)
    MatchScoreModel toDomain(PlayerDomain player1, PlayerDomain player2);
}
