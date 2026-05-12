package com.stubedavd.match.model.domain.score.impl;

import com.stubedavd.exception.BusinessException;
import com.stubedavd.match.model.domain.score.value.RegularGameScoreValue;
import com.stubedavd.player.model.domain.PlayerDomain;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegularGameScore {

    private final PlayerDomain player1;
    private final PlayerDomain player2;

    private PlayerDomain winner;

    private RegularGameScoreValue player1Score = RegularGameScoreValue.ZERO;
    private RegularGameScoreValue player2Score = RegularGameScoreValue.ZERO;

    public void addScore(PlayerDomain player) {

        if (isRoundFinished()) {
            throw new BusinessException("This round is already finished");
        }

        if (player1.equals(player)) {
            if (player2Score == RegularGameScoreValue.ADVANTAGE) {
                player2Score = RegularGameScoreValue.FORTY;
                return;
            }

            player1Score = scoreProcessing(player, player1Score);
        } else if (player2.equals(player)) {
            if (player1Score == RegularGameScoreValue.ADVANTAGE) {
                player1Score = RegularGameScoreValue.FORTY;
                return;
            }

            player2Score = scoreProcessing(player, player2Score);
        } else {
            throw new IllegalArgumentException("Player " + player + " is not in domain in this score");
        }
    }

    private RegularGameScoreValue scoreProcessing(PlayerDomain player, RegularGameScoreValue playerScore) {

        if (playerScore == RegularGameScoreValue.ADVANTAGE) {
            winner = player;
            return playerScore;
        }
        return RegularGameScoreValue
                .values()[(playerScore.ordinal() + 1) % RegularGameScoreValue.values().length];
    }

    public RegularGameScoreValue getScore(PlayerDomain player) {

        if (player1.equals(player)) {
            return player1Score;
        }

        if (player2.equals(player)) {
            return player2Score;
        }

        throw new IllegalArgumentException("Player " + player + " is not in domain in this score");
    }

    public boolean isRoundWon(PlayerDomain player) {

        if (winner != null) {
            return winner.equals(player);
        }

        RegularGameScoreValue winnerScore;
        RegularGameScoreValue loserScore;

        if (player1.equals(player)) {
            winnerScore = player1Score;
            loserScore = player2Score;
        } else if (player2.equals(player)) {
            winnerScore = player2Score;
            loserScore = player1Score;
        } else {
            throw new IllegalArgumentException("Player " + player + " is not in domain in this score");
        }

        return isMinPointsToWinReached(winnerScore) && isTwoPointsAdvantage(winnerScore, loserScore);
    }

    private boolean isRoundFinished() {
        return isRoundWon(player1) || isRoundWon(player2);
    }

    private boolean isTwoPointsAdvantage(RegularGameScoreValue winnerScore, RegularGameScoreValue loserScore) {
        return winnerScore.ordinal() > loserScore.ordinal() + 1;
    }

    private boolean isMinPointsToWinReached(RegularGameScoreValue score) {
        return score == RegularGameScoreValue.ADVANTAGE;
    }

}
