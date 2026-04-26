package com.stubedavd.match.service;

import com.stubedavd.player.entity.Player;
import com.stubedavd.exception.BusinessException;
import com.stubedavd.match.model.MatchGame;
import com.stubedavd.match.model.MatchPoint;
import com.stubedavd.match.model.MatchScoreModel;

public class MatchScoreCalculationService {

    public boolean isMatchFinished(MatchScoreModel matchScoreModel) {

        return matchScoreModel.isMatchFinished();
    }

    public void pointWon(MatchScoreModel matchScoreModel, Player player) {

        if (isMatchFinished(matchScoreModel)) {

            throw new BusinessException("Match already finished");
        }

        if (isTieBreak(matchScoreModel)) {

            tieBreakProcessing(matchScoreModel, player);
        } else {

            matchScoreModel.getPoints().addScore(player);

            if (isGameWon(matchScoreModel, player)) {

                gameWonProcessing(matchScoreModel, player);
            }
        }
    }

    private void gameWonProcessing(MatchScoreModel matchScoreModel, Player player) {

        matchScoreModel.setPoints(new MatchPoint());

        matchScoreModel.getGames().addScore(player);

        if (isSetWon(matchScoreModel, player)) {

            setWonProcessing(matchScoreModel, player);
        } else if (checkTieBreak(matchScoreModel)) {

            matchScoreModel.getPoints().setTieBreak(true);
        }
    }

    private void setWonProcessing(MatchScoreModel matchScoreModel, Player player) {

        recordScore(matchScoreModel);

        matchScoreModel.setGames(new MatchGame());

        matchScoreModel.getSets().addScore(player);

        if (isMatchWon(matchScoreModel, player)) {

            matchScoreModel.setMatchFinished(true);
            matchScoreModel.setWinner(player);
        }
    }

    private void recordScore(MatchScoreModel matchScoreModel) {

        matchScoreModel.getScore().add(matchScoreModel.getGames());
    }

    private void tieBreakProcessing(MatchScoreModel matchScoreModel, Player player) {

        matchScoreModel.getPoints().addScore(player);

        if (isTieBreakWon(matchScoreModel, player)) {

            matchScoreModel.setPoints(new MatchPoint());

            matchScoreModel.getGames().addScore(player);

            matchScoreModel.getPoints().setTieBreak(false);

            setWonProcessing(matchScoreModel, player);
        }
    }

    private boolean isTieBreak(MatchScoreModel matchScoreModel) {
        return matchScoreModel.getPoints().isTieBreak();
    }

    private boolean isGameWon(MatchScoreModel matchScoreModel, Player player) {

        return matchScoreModel.getPoints().isRoundWon(player);
    }

    private boolean isSetWon(MatchScoreModel matchScoreModel, Player player) {

        return matchScoreModel.getGames().isRoundWon(player);
    }

    private boolean isMatchWon(MatchScoreModel matchScoreModel, Player player) {

        return matchScoreModel.getSets().isRoundWon(player);
    }

    private boolean isTieBreakWon(MatchScoreModel matchScoreModel, Player player) {

        return matchScoreModel.getPoints().isRoundWon(player);
    }

    private boolean checkTieBreak(MatchScoreModel matchScoreModel) {

        return matchScoreModel.getGames().checkTieBreak();
    }
}
