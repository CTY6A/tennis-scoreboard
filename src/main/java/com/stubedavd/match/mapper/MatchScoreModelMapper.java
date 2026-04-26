package com.stubedavd.match.mapper;

import com.stubedavd.match.model.MatchScoreModel;
import com.stubedavd.player.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchScoreModelMapper {

    MatchScoreModelMapper INSTANCE = Mappers.getMapper(MatchScoreModelMapper.class);

    MatchScoreModel toModel(Player player1, Player player2);
}
