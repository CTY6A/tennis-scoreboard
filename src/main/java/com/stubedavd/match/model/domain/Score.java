package com.stubedavd.match.model.domain;

import com.stubedavd.exception.BusinessException;
import com.stubedavd.player.model.domain.PlayerDomain;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Score {

    private final PlayerDomain player1Domain;
    private final PlayerDomain player2Domain;

    private int player1Score = 0;
    private int player2Score = 0;

    public void addScore(PlayerDomain player) {

        if (isRoundWon(player1Domain) || isRoundWon(player2Domain)) {
            throw new BusinessException("This round is already finished");
        }

        if (player1Domain.equals(player)) {
            player1Score++;
        } else if (player2Domain.equals(player)) {
            player2Score++;
        } else {
            throw new IllegalArgumentException("Player " + player + " is not in domain in this score");
        }
    }

    public int getScore(PlayerDomain player) {

        if (player1Domain.equals(player)) {
            return player1Score;
        }

        if (player2Domain.equals(player)) {
            return player2Score;
        }

        throw new IllegalArgumentException("Player " + player + " is not in domain in this score");
    }

    public boolean isRoundWon(PlayerDomain player) {

        int winnerCounter;
        int loserCounter;

        if (player1Domain.equals(player)) {
            winnerCounter = player1Score;
            loserCounter = player2Score;
        } else if (player2Domain.equals(player)) {
            winnerCounter = player2Score;
            loserCounter = player1Score;
        } else {
            throw new IllegalArgumentException("Player " + player + " is not in domain in this score");
        }

        return isAdvantageLimitReached(winnerCounter) && isTwoPointsAdvantage(winnerCounter, loserCounter);
    }

    private boolean isTwoPointsAdvantage(int winnerScore, int loserScore) {
        return winnerScore > loserScore + 1;
    }

    private boolean isAdvantageLimitReached(int score) {
        return score >= getAdvantageLimit();
    }

    protected boolean isTieBreak() {

        return isAdvantageLimitReached(player1Score) && isAdvantageLimitReached(player2Score);
    }

    protected abstract int getAdvantageLimit();
}
