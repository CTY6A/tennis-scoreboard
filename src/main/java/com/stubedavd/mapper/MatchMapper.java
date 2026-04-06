package com.stubedavd.mapper;

import com.stubedavd.dto.MatchDto;
import com.stubedavd.dto.PlayerDto;
import com.stubedavd.dto.response.MatchResponseDto;
import com.stubedavd.dto.request.PlayerRequestDto;
import com.stubedavd.dto.response.PlayerResponseDto;
import com.stubedavd.model.MatchScoreModel;
import com.stubedavd.dto.request.MatchRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface MatchMapper {

    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    MatchRequestDto toMatchRequestDto(PlayerRequestDto playerRequestDto1, PlayerRequestDto playerRequestDto2);

    MatchDto toMatchDto(PlayerDto playerDto1, PlayerDto playerDto2);

    MatchResponseDto toMatchResponseDto(
            UUID uuid,
            PlayerResponseDto playerResponseDto1,
            PlayerResponseDto playerResponseDto2,
            MatchScoreModel matchScoreModel
    );
}
