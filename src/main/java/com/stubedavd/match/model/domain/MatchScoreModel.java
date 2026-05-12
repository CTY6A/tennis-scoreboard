package com.stubedavd.match.model.domain;

import com.stubedavd.match.model.domain.score.impl.MatchScore;
import com.stubedavd.match.model.domain.score.impl.RegularGameScore;
import com.stubedavd.match.model.domain.score.impl.SetScore;
import com.stubedavd.match.model.domain.score.impl.TiebreakScore;
import com.stubedavd.player.model.domain.PlayerDomain;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@ToString
public class MatchScoreModel {

    private final PlayerDomain player1;
    private final PlayerDomain player2;

    private PlayerDomain winner;

    private RegularGameScore regularGameScore;
    private TiebreakScore tiebreakScore;
    private SetScore setScore;
    private final MatchScore matchScore;

    private final List<SetScore> protocol;

    public MatchScoreModel(PlayerDomain player1, PlayerDomain player2) {

        this.player1 = player1;
        this.player2 = player2;
        this.winner = null;

        this.regularGameScore = new RegularGameScore(player1, player2);
        this.tiebreakScore = new TiebreakScore(player1, player2);
        this.setScore = new SetScore(player1, player2);
        this.matchScore = new MatchScore(player1, player2);

        this.protocol = new LinkedList<>();
    }

    public void pointWon(PlayerDomain player) {
        
        if (isTieBreak()) {
            tieBreakProcessing(player);
            return;
        }

        regularGameScore.addScore(player);

        if (isGameWon(player)) {
            gameWonProcessing(player);
        }

    }

    private void gameWonProcessing(PlayerDomain player) {

        regularGameScore = new RegularGameScore(player1, player2);

        setScore.addScore(player);

        if (isSetWon(player)) {
            setWonProcessing(player);
        }
    }

    private void setWonProcessing(PlayerDomain player) {

        recordScore();

        setScore = new SetScore(player1, player2);

        matchScore.addScore(player);

        if (isMatchWon(player)) {
            winner = player;
        }
    }

    private void recordScore() {
        protocol.add(setScore);
    }

    private void tieBreakProcessing(PlayerDomain player) {

        tiebreakScore.addScore(player);

        if (isTieBreakWon(player)) {

            regularGameScore = new RegularGameScore(player1, player2);
            tiebreakScore = new TiebreakScore(player1, player2);

            setScore.addScore(player);

            setWonProcessing(player);
        }
    }

    private boolean isTieBreak() {
        return setScore.isTieBreak();
    }

    private boolean isGameWon(PlayerDomain player) {
        return regularGameScore.isRoundWon(player);
    }

    private boolean isSetWon(PlayerDomain player) {
        return setScore.isRoundWon(player);
    }

    private boolean isMatchWon(PlayerDomain player) {
        return matchScore.isRoundWon(player);
    }

    private boolean isTieBreakWon(PlayerDomain player) {
        return tiebreakScore.isRoundWon(player);
    }
}
