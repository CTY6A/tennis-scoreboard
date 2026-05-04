package com.stubedavd.match.model;

import com.stubedavd.player.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class Score {

    public static final int ZERO_POINTS = 0;

    // TODO: Класс хранит ссылки на JPA-сущности (`Player`). Использование объектов JPA Entity в доменной логике
        // создаёт прямую зависимость доменного слоя от слоя персистентности (долговременного хранения данных)
        // и смешивает слои приложения, что нарушает чистоту архитектуры.
        // Это может привести к проблемам с ленивой загрузкой (`LazyInitializationException`)
        // или к неожиданным изменениям в базе данных, если состояние `Player` будет изменено в ходе бизнес-логики.
        // Доменные модели должны оперировать другими доменными моделями, а не сущностями, привязанными к базе данных.

    // Хотя `Map<Player, Integer>` является более гибким решением, использование `Map` для двух предопределённых, фиксированных ключей является излишним усложнением (over-engineering).
        // `Map` подразумевает динамическое количество ключей, но в теннисе всегда ровно два игрока (или две стороны), поэтому лучше использовать просто два отдельных поля.

    private final Map<Player, Integer> score = new HashMap<>();

    // TODO: Метод должен принимать доменную модель игрока, а не JPA Entity
    public void addScore(Player player) {

        // TODO: Перед начислением очка нет проверки на то, что текущий уровень игры ещё не завершён.

        score.merge(player, 1, Integer::sum);
    }

    // TODO: Метод должен принимать доменную модель игрока, а не JPA Entity
    public int getScore(Player player) {

        return score.getOrDefault(player, ZERO_POINTS);
    }

    // TODO: Метод должен принимать доменную модель игрока, а не JPA Entity
    public boolean isRoundWon(Player player) {

        int winnerCounter = 0;
        int loserCounter = 0;

        for(Map.Entry<Player, Integer> entry : score.entrySet()) {

            if (player.equals(entry.getKey())) {
                winnerCounter = entry.getValue();
            } else {
                loserCounter = entry.getValue();
            }
        }

        // Выражение в return сложно читается. Приходится догадываться, что означает '> loserCounter + 1'.
            // Лучше вынести его во вспомогательный метод с понятным названием.
        return winnerCounter >= getAdvantageLimit() && winnerCounter > loserCounter + 1;
    }

    // Из названия checkTieBreak не понятно, что это за "проверка тай-брейка"
    protected boolean checkTieBreak() {

        // можно записать так:
//        return score.values().stream()
//                .allMatch(value -> value == getAdvantageLimit());

        for (Map.Entry<Player, Integer> entry : score.entrySet()) {

            // Условие из if лучше вынести во вспомогательный метод с понятным названием.
            if (entry.getValue() != getAdvantageLimit()) {
                return false;
            }
        }

        return true;
    }

    protected abstract int getAdvantageLimit();
}
