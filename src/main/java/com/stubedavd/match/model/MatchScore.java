package com.stubedavd.match.model;

import com.stubedavd.player.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class MatchScore {

    private final Map<Player, Integer> score = new HashMap<>();

    public void addScore(Player player) {

        score.put(player, score.getOrDefault(player, 0) + 1);
    }

    public int getScore(Player player) {

        return score.getOrDefault(player, 0);
    }

    public boolean isRoundWon(Player player) {

        Integer winnerCounter = 0;
        Integer loserCounter = 0;

        for(Map.Entry<Player, Integer> entry : score.entrySet()) {

            if (player.equals(entry.getKey())) {
                winnerCounter = entry.getValue();
            } else {
                loserCounter = entry.getValue();
            }
        }

        return winnerCounter >= getAdvantageLimit() && winnerCounter > loserCounter + 1;
    }

    protected abstract int getAdvantageLimit();

    protected boolean checkTieBreak() {

        for (Map.Entry<Player, Integer> entry : score.entrySet()) {

            if (entry.getValue() != getAdvantageLimit()) {
                return false;
            }
        }

        return true;
    }
}
