package com.stubedavd.model;

import com.stubedavd.entity.Player;
import com.stubedavd.logic.MatchGame;
import com.stubedavd.logic.MatchPoint;
import com.stubedavd.logic.MatchSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class MatchScoreModel {

    private final Player player1;
    private final Player player2;

    private Player winner;

    private MatchPoint points;
    private MatchGame games;
    private MatchSet sets;

    private final List<MatchGame> score;

    private Boolean matchFinished;

    public MatchScoreModel(Player player1, Player player2) {

        this.player1 = player1;
        this.player2 = player2;
        this.winner = null;

        this.points = new MatchPoint();
        this.games = new MatchGame();
        this.sets = new MatchSet();

        this.score = new LinkedList<>();

        this.matchFinished = false;
    }
}
