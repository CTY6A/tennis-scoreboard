package com.stubedavd.service.impl;

import com.stubedavd.dto.PlayerDto;
import com.stubedavd.exception.BusinessException;
import com.stubedavd.model.MatchScoreModel;
import com.stubedavd.service.MatchScoreCalculationService;

import java.util.Map;

public class MatchScoreCalculationServiceImpl implements MatchScoreCalculationService {

    public static final int GAME_ADVANTAGE_LIMIT = 4;
    public static final int SET_ADVANTAGE_LIMIT = 6;
    public static final int TIE_BREAK_ADVANTAGE_LIMIT = 7;

    @Override
    public Boolean isMatchFinished(MatchScoreModel matchScoreModel) {

        return matchScoreModel.getMatchFinished();
    }

    @Override
    public void pointWon(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        if (isMatchFinished(matchScoreModel)) {

            throw new BusinessException("Match already finished");
        }

        if (isTieBreak(matchScoreModel)) {

            tieBreakProcessing(matchScoreModel, playerDto);
        } else {

            addScore(matchScoreModel.getPoints(), playerDto);

            if (isGameWon(matchScoreModel, playerDto)) {

                gameWonProcessing(matchScoreModel, playerDto);
            }
        }
    }

    private void gameWonProcessing(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        resetScore(matchScoreModel.getPoints());

        addScore(matchScoreModel.getGames(), playerDto);

        if (isSetWon(matchScoreModel, playerDto)) {

            setWonProcessing(matchScoreModel, playerDto);
        } else if (checkTieBreak(matchScoreModel)) {

            matchScoreModel.setTieBreak(true);
        }
    }

    private void setWonProcessing(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        resetScore(matchScoreModel.getGames());

        addScore(matchScoreModel.getSets(), playerDto);

        if (isMatchWon(matchScoreModel, playerDto)) {

            matchScoreModel.setMatchFinished(true);
            matchScoreModel.setWinner(playerDto);
        }
    }

    private void tieBreakProcessing(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        addScore(matchScoreModel.getPoints(), playerDto);

        if (isTieBreakWon(matchScoreModel, playerDto)) {

            resetScore(matchScoreModel.getPoints());
            matchScoreModel.setTieBreak(false);

            setWonProcessing(matchScoreModel, playerDto);
        }
    }

    private Boolean isTieBreak(MatchScoreModel matchScoreModel) {
        return matchScoreModel.getTieBreak();
    }

    private Boolean isGameWon(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        return isPlayerWonThisPart(
                matchScoreModel.getPoints(),
                playerDto,
                GAME_ADVANTAGE_LIMIT
        );
    }

    private Boolean isSetWon(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        return isPlayerWonThisPart(
                matchScoreModel.getGames(),
                playerDto,
                SET_ADVANTAGE_LIMIT
        );
    }

    private Boolean isMatchWon(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        return matchScoreModel.getSets().get(playerDto) == 2;
    }

    private Boolean isTieBreakWon(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        return isPlayerWonThisPart(
                matchScoreModel.getPoints(),
                playerDto,
                TIE_BREAK_ADVANTAGE_LIMIT
        );
    }

    private Boolean isPlayerWonThisPart(
            Map<PlayerDto, Integer> calculationSubject,
            PlayerDto playerDto,
            Integer advantageLimit
    ) {

        Integer winnerCounter = 0;
        Integer loserCounter = 0;

        for(Map.Entry<PlayerDto, Integer> entry : calculationSubject.entrySet()) {

            if (playerDto.equals(entry.getKey())) {
                winnerCounter = entry.getValue();
            } else {
                loserCounter = entry.getValue();
            }
        }

        return winnerCounter >= advantageLimit && winnerCounter > loserCounter + 1;
    }

    private Boolean checkTieBreak(MatchScoreModel matchScoreModel) {

        for (Map.Entry<PlayerDto, Integer> entry : matchScoreModel.getGames().entrySet()) {

            if (entry.getValue() != SET_ADVANTAGE_LIMIT) {
                return false;
            }
        }

        return true;
    }

    private void addScore(Map<PlayerDto, Integer> score, PlayerDto playerDto) {

        score.put(playerDto, score.get(playerDto) + 1);
    }

    private void resetScore(Map<PlayerDto, Integer> score) {

        score.replaceAll((key, value) -> 0);
    }
}
