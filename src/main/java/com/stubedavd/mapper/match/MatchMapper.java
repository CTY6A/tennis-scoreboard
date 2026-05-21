package com.stubedavd.mapper.match;

import com.stubedavd.model.match.dto.request.MatchRequestDto;
import com.stubedavd.model.match.dto.response.MatchResponseDto;
import com.stubedavd.model.match.dto.response.MatchesResponseDto;
import com.stubedavd.model.match.entity.Match;
import com.stubedavd.model.player.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MatchMapper {

    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mapping(target = "id", ignore = true)
    Match toEntity(Player player1, Player player2, Player winner);

    MatchResponseDto toResponseDto(Match match);

    List<MatchResponseDto> toResponseDtoList(List<Match> matches);

    MatchRequestDto toRequestDto(String player1Name, String player2Name);

    MatchesResponseDto toMatchesResponseDto(
            List<MatchResponseDto> matches,
            String playerName,
            long pageCount,
            long pageNumber,
            long matchesCount,
            long matchesFrom,
            long matchesTo
    );

    @ObjectFactory
    default MatchResponseDto createResponseDto (Match match) {
        return new MatchResponseDto(
                match.getPlayer1().getName(),
                match.getPlayer2().getName(),
                match.getWinner().getName()
        );
    }

    @ObjectFactory
    default Match createMatch(Player player1, Player player2, Player winner) {
        return new Match(player1, player2, winner);
    }
}
