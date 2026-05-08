package com.stubedavd.match.model.domain;

import com.stubedavd.match.model.domain.score.impl.MatchScore;
import com.stubedavd.match.model.domain.score.impl.RegularGameScore;
import com.stubedavd.match.model.domain.score.impl.SetScore;
import com.stubedavd.match.model.domain.score.impl.TiebreakScore;
import com.stubedavd.player.model.domain.PlayerDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter // TODO: сеттеры позволяют бесконтрольно изменять состояние модели
@ToString
public class MatchScoreModel {

    // TODO: Класс хранит ссылки на JPA-сущности (`Player`). Использование объектов JPA Entity в доменной логике
        // создаёт прямую зависимость доменного слоя от слоя персистентности (долговременного хранения данных)
        // и смешивает слои приложения, что нарушает чистоту архитектуры.
        // Это может привести к проблемам с ленивой загрузкой (`LazyInitializationException`)
        // или к неожиданным изменениям в базе данных, если состояние `Player` будет изменено в ходе бизнес-логики.
        // Доменные модели должны оперировать другими доменными моделями, а не сущностями, привязанными к базе данных.

    // TODO: Класс является анемичной моделью — он является лишь контейнером для данных, а значительная часть логики находится в сервисном слое.
        // Если бы у класса вместо простых сеттеров были методы, совершающие необходимую работу над полями,
        // это больше соответствовало бы ООП стилю и обязанности класса (в роли доменной модели).
        // Также, эту часть логики было бы легче тестировать.
        // (см. файл "Анемичная vs Богатая модель предметной области.md" в этом же пакете)

    private final PlayerDomain player1;
    private final PlayerDomain player2;

    private PlayerDomain winner;

    private RegularGameScore regularGameScore;
    private TiebreakScore tiebreakScore;
    private SetScore setScore;
    private MatchScore matchScore;

    private final List<SetScore> score;

    private boolean matchFinished;

    public MatchScoreModel(PlayerDomain player1, PlayerDomain player2) {

        this.player1 = player1;
        this.player2 = player2;
        this.winner = null;

        this.regularGameScore = new RegularGameScore(player1, player2);
        this.tiebreakScore = new TiebreakScore(player1, player2);
        this.setScore = new SetScore(player1, player2);
        this.matchScore = new MatchScore(player1, player2);

        this.score = new LinkedList<>();

        this.matchFinished = false;
    }
}
