package com.stubedavd.model;

import com.stubedavd.dto.PlayerDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ToString
public class MatchScoreModel {

    private PlayerDto playerDto1;
    private PlayerDto playerDto2;
    private PlayerDto winner;

    private Map<PlayerDto, Integer> points;
    private Map<PlayerDto, Integer> games;
    private Map<PlayerDto, Integer> sets;

    private Boolean tieBreak;
    private Boolean matchFinished;

    public MatchScoreModel(PlayerDto playerDto1, PlayerDto playerDto2) {

        this.playerDto1 = playerDto1;
        this.playerDto2 = playerDto2;
        this.winner = null;

        this.points = new HashMap<>() {{
            put(playerDto1, 0);
            put(playerDto2, 0);
        }};

        this.games = new HashMap<>() {{
            put(playerDto1, 0);
            put(playerDto2, 0);
        }};

        this.sets = new HashMap<>() {{
            put(playerDto1, 0);
            put(playerDto2, 0);
        }};

        this.tieBreak = false;
        this.matchFinished = false;
    }
}
