package com.stubedavd.match.model;

public class MatchGame extends Score {

    // TODO: Класс является анемичной моделью — он является лишь контейнером для данных, а значительная часть логики находится в сервисном слое.
        // Если бы у класса были методы, совершающие необходимую работу над полями,
        // это больше соответствовало бы ООП стилю и обязанности класса (в роли доменной модели).
        // Также, эту часть логики было бы легче тестировать.
        // (см. файл "Анемичная vs Богатая модель предметной области.md" в этом же пакете)

    // Класс называется MatchGame — по типу счёта, за который он отвечает. Название SetScore было бы более интуитивно понятным.

    // Константа GAME_ADVANTAGE_LIMIT используется только внутри этого класса — ей достаточно быть private
    // Можно назвать MIN_POINTS_TO_WIN
    public static final int GAME_ADVANTAGE_LIMIT = 6;

    @Override
    protected int getAdvantageLimit() {
        return GAME_ADVANTAGE_LIMIT;
    }

    @Override
    public boolean checkTieBreak() {

        return super.checkTieBreak();
    }
}
