package com.stubedavd.match.model.domain.score.impl;

import com.stubedavd.match.model.domain.score.Score;
import com.stubedavd.player.model.domain.PlayerDomain;

public class MatchScore extends Score {

    // TODO: Класс является анемичной моделью — он является лишь контейнером для данных, а значительная часть логики находится в сервисном слое.
        // Если бы у класса были методы, совершающие необходимую работу над полями,
        // это больше соответствовало бы ООП стилю и обязанности класса (в роли доменной модели).
        // Также, эту часть логики было бы легче тестировать.
        // (см. файл "Анемичная vs Богатая модель предметной области.md" в этом же пакете)

    private static final int MIN_POINTS_TO_WIN = 2;

    public MatchScore(PlayerDomain player1Domain, PlayerDomain player2Domain) {
        super(player1Domain, player2Domain);
    }

    @Override
    protected int getAdvantageLimit() {
        return MIN_POINTS_TO_WIN;
    }
}
