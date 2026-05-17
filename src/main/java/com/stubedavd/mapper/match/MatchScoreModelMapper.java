package com.stubedavd.mapper.match;

import com.stubedavd.model.match.domain.MatchScoreModel;
import com.stubedavd.model.player.domain.PlayerDomain;
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
