package com.stubedavd.mapper.match;

import com.stubedavd.model.match.domain.MatchScoreModel;
import com.stubedavd.model.match.domain.score.value.RegularGameScoreValue;
import com.stubedavd.model.match.dto.response.FinalScoreResponseDto;
import com.stubedavd.model.match.dto.response.MatchScoreResponseDto;
import com.stubedavd.model.player.domain.PlayerDomain;
import com.stubedavd.model.player.dto.response.PlayerResponseDto;
import com.stubedavd.model.player.dto.response.PlayerScoreResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchScoreMapper {

    MatchScoreMapper INSTANCE = Mappers.getMapper(MatchScoreMapper.class);

    @Mapping(target = "winner", ignore = true)
    @Mapping(target = "regularGameScore", ignore = true)
    @Mapping(target = "tiebreakScore", ignore = true)
    @Mapping(target = "setScore", ignore = true)
    @Mapping(target = "matchScore", ignore = true)
    @Mapping(target = "protocol", ignore = true)
    MatchScoreModel toDomain(PlayerDomain player1, PlayerDomain player2);

    MatchScoreResponseDto toResponseDto(MatchScoreModel matchScoreModel);

    FinalScoreResponseDto toFinalResponseDto(MatchScoreModel matchScoreModel);

    @ObjectFactory
    default FinalScoreResponseDto createFinalResponseDto(MatchScoreModel matchScoreModel) {
        return new FinalScoreResponseDto(
                new PlayerScoreResponseDto(
                        matchScoreModel.getPlayer1().name(),
                        matchScoreModel
                                .getProtocol()
                                .stream()
                                .map(matchGame -> matchGame.getScore(matchScoreModel.getPlayer1()))
                                .toList()
                ),
                new PlayerScoreResponseDto(
                        matchScoreModel.getPlayer2().name(),
                        matchScoreModel
                                .getProtocol()
                                .stream()
                                .map(matchGame -> matchGame.getScore(matchScoreModel.getPlayer2()))
                                .toList()
                )
        );
    }

    @ObjectFactory
    default MatchScoreResponseDto createMatchResponseDto(MatchScoreModel matchScoreModel) {
        return new MatchScoreResponseDto(
                new PlayerResponseDto(
                        matchScoreModel.getPlayer1().id(),
                        matchScoreModel.getPlayer1().name(),
                        matchScoreModel.getMatchScore().getScore(matchScoreModel.getPlayer1()),
                        matchScoreModel.getSetScore().getScore(matchScoreModel.getPlayer1()),
                        getPointsString(
                                matchScoreModel,
                                matchScoreModel.getPlayer1()
                        )
                ),
                new PlayerResponseDto(
                        matchScoreModel.getPlayer2().id(),
                        matchScoreModel.getPlayer2().name(),
                        matchScoreModel.getMatchScore().getScore(matchScoreModel.getPlayer2()),
                        matchScoreModel.getSetScore().getScore(matchScoreModel.getPlayer2()),
                        getPointsString(
                                matchScoreModel,
                                matchScoreModel.getPlayer2()
                        )
                )
        );
    }

    private String getPointsString(MatchScoreModel matchScoreModel, PlayerDomain player1) {
        if (matchScoreModel.getSetScore().isTieBreak()) {
            return String.valueOf(matchScoreModel.getTiebreakScore().getScore(player1));
        }
        RegularGameScoreValue player1Points = matchScoreModel.getRegularGameScore().getScore(player1);
        return player1Points.toString();
    }
}
