package com.stubedavd.model;

import com.stubedavd.dto.PlayerDto;
import com.stubedavd.dto.response.PlayerResponseDto;
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

    private Map<PlayerDto, Integer> score;
    private Map<PlayerDto, Integer> game;
    private Map<PlayerDto, Integer> set;

    public MatchScoreModel(PlayerDto playerDto1, PlayerDto playerDto2) {

        this.playerDto1 = playerDto1;
        this.playerDto2 = playerDto2;
        this.winner = null;

        this.score = new HashMap<>() {{
            put(playerDto1, 0);
            put(playerDto2, 0);
        }};

        this.game = new HashMap<>() {{
            put(playerDto1, 0);
            put(playerDto2, 0);
        }};

        this.set = new HashMap<>() {{
            put(playerDto1, 0);
            put(playerDto2, 0);
        }};
    }
}
