package com.stubedavd.match.model.service;

import com.stubedavd.match.model.domain.MatchScoreModel;
import com.stubedavd.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OngoingMatchService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Класс отвечает за создание объекта текущего матча (доменной модели).
        // При этом он способствует смешению слоёв — сам использует зависимость от DAO и передаёт JPA Entity в доменную модель.
        // (см. файл "Принцип разделения ответственности (Separation of Concerns).md" в этом же пакете)
        // Этому классу должна быть не нужна зависимость PlayerRepositoryImpl.
        // А также создание нового матча можно перенести в OngoingMatchService и удалить этот сервис.

    private final Map<UUID, MatchScoreModel> ongoingMatches;

    public OngoingMatchService() {

        // TODO: Веб-приложения по своей природе являются многопоточными.
            // Поэтому стоит использовать потокобезопасную реализацию `Map`, специально предназначенную для многопоточной среды.
            // Лучшим выбором здесь является `java.util.concurrent.ConcurrentHashMap`.
        this.ongoingMatches = new HashMap<>();
    }

    public UUID save(MatchScoreModel matchScoreModel) {

        UUID uuid = UUID.randomUUID();

        ongoingMatches.put(uuid, matchScoreModel);

        return uuid;
    }

    public MatchScoreModel get(UUID uuid) {

        MatchScoreModel matchScoreModel = ongoingMatches.get(uuid);

        if (matchScoreModel == null) {

            throw new NotFoundException("Match not found");
        }

        return matchScoreModel;
    }

    public void delete(UUID uuid) {

        ongoingMatches.remove(uuid);
    }
}
