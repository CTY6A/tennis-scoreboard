package com.stubedavd.match.model;

import com.stubedavd.player.entity.Player;
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

    private final Player player1;
    private final Player player2;

    private Player winner;

    private MatchPoint points;
    private MatchGame games;
    private MatchSet sets;

    private final List<MatchGame> score;

    private boolean matchFinished;

    public MatchScoreModel(Player player1, Player player2) {

        this.player1 = player1;
        this.player2 = player2;
        this.winner = null;

        this.points = new MatchPoint();
        this.games = new MatchGame();
        this.sets = new MatchSet();

        this.score = new LinkedList<>();

        this.matchFinished = false;
    }
}
