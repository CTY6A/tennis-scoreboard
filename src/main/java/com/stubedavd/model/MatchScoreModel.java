package com.stubedavd.model;

import com.stubedavd.entity.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ToString
public class MatchScoreModel {

    private Player player1;
    private Player player2;
    private Player winner;

    private Map<Player, Integer> points;
    private Map<Player, Integer> games;
    private Map<Player, Integer> sets;

    private Boolean tieBreak;
    private Boolean matchFinished;

    public MatchScoreModel(Player player1, Player player2) {

        this.player1 = player1;
        this.player2 = player2;
        this.winner = null;

        this.points = new HashMap<>() {{
            put(player1, 0);
            put(player2, 0);
        }};

        this.games = new HashMap<>() {{
            put(player1, 0);
            put(player2, 0);
        }};

        this.sets = new HashMap<>() {{
            put(player1, 0);
            put(player2, 0);
        }};

        this.tieBreak = false;
        this.matchFinished = false;
    }
}
