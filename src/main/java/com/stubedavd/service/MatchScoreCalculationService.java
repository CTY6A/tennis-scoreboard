package com.stubedavd.service;

import com.stubedavd.entity.Player;
import com.stubedavd.exception.BusinessException;
import com.stubedavd.model.MatchScoreModel;

import java.util.List;
import java.util.Map;

public class MatchScoreCalculationService {

    public static final int GAME_ADVANTAGE_LIMIT = 4;
    public static final int SET_ADVANTAGE_LIMIT = 6;
    public static final int TIE_BREAK_ADVANTAGE_LIMIT = 7;

    public Boolean isMatchFinished(MatchScoreModel matchScoreModel) {

        return matchScoreModel.getMatchFinished();
    }

    public void pointWon(MatchScoreModel matchScoreModel, Player player) {

        if (isMatchFinished(matchScoreModel)) {

            throw new BusinessException("Match already finished");
        }

        if (isTieBreak(matchScoreModel)) {

            tieBreakProcessing(matchScoreModel, player);
        } else {

            addScore(matchScoreModel.getPoints(), player);

            if (isGameWon(matchScoreModel, player)) {

                gameWonProcessing(matchScoreModel, player);
            }
        }
    }

    private void gameWonProcessing(MatchScoreModel matchScoreModel, Player player) {

        resetScore(matchScoreModel.getPoints());

        addScore(matchScoreModel.getGames(), player);

        if (isSetWon(matchScoreModel, player)) {

            setWonProcessing(matchScoreModel, player);
        } else if (checkTieBreak(matchScoreModel)) {

            matchScoreModel.setTieBreak(true);
        }
    }

    private void setWonProcessing(MatchScoreModel matchScoreModel, Player player) {

        recordScore(matchScoreModel);

        resetScore(matchScoreModel.getGames());

        addScore(matchScoreModel.getSets(), player);

        if (isMatchWon(matchScoreModel, player)) {

            matchScoreModel.setMatchFinished(true);
            matchScoreModel.setWinner(player);
        }
    }

    private void recordScore(MatchScoreModel matchScoreModel) {

        for (Map.Entry<Player, Integer> playerGames : matchScoreModel.getGames().entrySet()) {

            Map<Player, List<Integer>> score = matchScoreModel.getScore();

            List<Integer> playerScore = score.get(playerGames.getKey());

            Integer games = playerGames.getValue();

            playerScore.add(games);
        }
    }

    private void tieBreakProcessing(MatchScoreModel matchScoreModel, Player player) {

        addScore(matchScoreModel.getPoints(), player);

        if (isTieBreakWon(matchScoreModel, player)) {

            resetScore(matchScoreModel.getPoints());

            addScore(matchScoreModel.getGames(), player);

            matchScoreModel.setTieBreak(false);

            setWonProcessing(matchScoreModel, player);
        }
    }

    private Boolean isTieBreak(MatchScoreModel matchScoreModel) {
        return matchScoreModel.getTieBreak();
    }

    private Boolean isGameWon(MatchScoreModel matchScoreModel, Player player) {

        return isPlayerWonThisPart(
                matchScoreModel.getPoints(),
                player,
                GAME_ADVANTAGE_LIMIT
        );
    }

    private Boolean isSetWon(MatchScoreModel matchScoreModel, Player player) {

        return isPlayerWonThisPart(
                matchScoreModel.getGames(),
                player,
                SET_ADVANTAGE_LIMIT
        );
    }

    private Boolean isMatchWon(MatchScoreModel matchScoreModel, Player player) {

        return matchScoreModel.getSets().get(player) == 2;
    }

    private Boolean isTieBreakWon(MatchScoreModel matchScoreModel, Player player) {

        return isPlayerWonThisPart(
                matchScoreModel.getPoints(),
                player,
                TIE_BREAK_ADVANTAGE_LIMIT
        );
    }

    private Boolean isPlayerWonThisPart(
            Map<Player, Integer> calculationSubject,
            Player playerDto,
            Integer advantageLimit
    ) {

        Integer winnerCounter = 0;
        Integer loserCounter = 0;

        for(Map.Entry<Player, Integer> entry : calculationSubject.entrySet()) {

            if (playerDto.equals(entry.getKey())) {
                winnerCounter = entry.getValue();
            } else {
                loserCounter = entry.getValue();
            }
        }

        return winnerCounter >= advantageLimit && winnerCounter > loserCounter + 1;
    }

    private Boolean checkTieBreak(MatchScoreModel matchScoreModel) {

        for (Map.Entry<Player, Integer> entry : matchScoreModel.getGames().entrySet()) {

            if (entry.getValue() != SET_ADVANTAGE_LIMIT) {
                return false;
            }
        }

        return true;
    }

    private void addScore(Map<Player, Integer> score, Player player) {

        score.put(player, score.get(player) + 1);
    }

    private void resetScore(Map<Player, Integer> score) {

        score.replaceAll((key, value) -> 0);
    }
}
