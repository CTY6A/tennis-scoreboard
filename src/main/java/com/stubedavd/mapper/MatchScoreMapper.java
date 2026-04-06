package com.stubedavd.mapper;

import com.stubedavd.dto.PlayerDto;
import com.stubedavd.model.MatchScoreModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchScoreMapper {

    MatchScoreMapper INSTANCE = Mappers.getMapper(MatchScoreMapper.class);

    MatchScoreModel toModel(PlayerDto playerDto1, PlayerDto playerDto2);
}
