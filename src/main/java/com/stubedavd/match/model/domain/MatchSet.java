package com.stubedavd.match.model.domain;

import com.stubedavd.player.model.domain.PlayerDomain;

public class MatchSet extends Score {

    // TODO: Класс является анемичной моделью — он является лишь контейнером для данных, а значительная часть логики находится в сервисном слое.
        // Если бы у класса были методы, совершающие необходимую работу над полями,
        // это больше соответствовало бы ООП стилю и обязанности класса (в роли доменной модели).
        // Также, эту часть логики было бы легче тестировать.
        // (см. файл "Анемичная vs Богатая модель предметной области.md" в этом же пакете)

    // Класс называется MatchSet — по типу счёта, за который он отвечает. Название Score было бы более интуитивно понятным.

    // Константа MATCH_ADVANTAGE_LIMIT используется только внутри этого класса — ей достаточно быть private
    // Можно назвать MIN_POINTS_TO_WIN
    public static final int MATCH_ADVANTAGE_LIMIT = 2;

    public MatchSet(PlayerDomain player1Domain, PlayerDomain player2Domain) {
        super(player1Domain, player2Domain);
    }

    @Override
    protected int getAdvantageLimit() {
        return MATCH_ADVANTAGE_LIMIT;
    }
}
