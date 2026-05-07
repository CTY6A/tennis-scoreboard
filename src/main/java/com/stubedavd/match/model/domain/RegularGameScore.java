package com.stubedavd.match.model.domain;

import com.stubedavd.player.model.domain.PlayerDomain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // TODO: сеттеры позволяют бесконтрольно изменять состояние модели
public class RegularGameScore extends Score {

    // TODO: Класс "кодирует" счёт в гейме, что способствует процедурному стилю. Поскольку в гейме особый счёт,
        // ООП подходом было бы создать специальный enum с константами ZERO, FIFTEEN, THIRTY, FORTY, ADVANTAGE для хранения счёта в гейме.

    // TODO: За тай-брейк отвечает поле boolean tieBreak, хотя тай-брейк это не состояние гейма, а его особый вид со своими правилами.
        // Это нарушает Принцип единой ответственности (SRP). Стоит реализовать его в отдельной абстракции.

    // TODO: Класс является анемичной моделью — он является лишь контейнером для данных, а значительная часть логики находится в сервисном слое.
        // Если бы у класса вместо простых сеттеров были методы, совершающие необходимую работу над полями,
        // это больше соответствовало бы ООП стилю и обязанности класса (в роли доменной модели).
        // Также, эту часть логики было бы легче тестировать.
        // (см. файл "Анемичная vs Богатая модель предметной области.md" в этом же пакете)

    // Класс называется RegularGameScore — по типу счёта, за который он отвечает. Название RegularGameScore (и TiebreakScore) было бы более интуитивно понятным.

    // Можно назвать MIN_POINTS_TO_WIN
    public static final int POINTS_ADVANTAGE_LIMIT = 4;

    public RegularGameScore(PlayerDomain player1Domain, PlayerDomain player2Domain) {
        super(player1Domain, player2Domain);
    }

    @Override
    protected int getAdvantageLimit() {
        return POINTS_ADVANTAGE_LIMIT;
    }
}
