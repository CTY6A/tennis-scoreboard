package com.stubedavd.match.service;

import com.stubedavd.match.mapper.MatchScoreModelMapper;
import com.stubedavd.match.model.MatchScoreModel;
import com.stubedavd.player.dto.request.PlayerRequestDto;
import com.stubedavd.player.entity.Player;
import com.stubedavd.player.mapper.PlayerMapper;
import com.stubedavd.player.repository.PlayerRepository;

import java.util.UUID;

public class NewMatchService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Класс отвечает за создание объекта текущего матча (доменной модели).
        // При этом он способствует смешению слоёв — сам использует зависимость от DAO и передаёт JPA Entity в доменную модель.
        // (см. файл "Принцип разделения ответственности (Separation of Concerns).md" в этом же пакете)
        // Этому классу должна быть не нужна зависимость PlayerRepository.
        // А также создание нового матча можно перенести в OngoingMatchService и удалить этот сервис.

    private final OngoingMatchService ongoingMatchService;

    private final PlayerMapper playerMapper;
    private final MatchScoreModelMapper matchScoreModelMapper;

    private final PlayerRepository playerRepository;

    // можно использовать @RequiredArgsConstructor над классом вместо самописного конструктора
    public NewMatchService(
            OngoingMatchService ongoingMatchService,
            PlayerMapper playerMapper,
            MatchScoreModelMapper matchScoreModelMapper,
            PlayerRepository playerRepository
    ) {

        this.ongoingMatchService = ongoingMatchService;
        this.playerMapper = playerMapper;
        this.matchScoreModelMapper = matchScoreModelMapper;
        this.playerRepository = playerRepository;
    }

    // Метод создания матча может принимать один DTO, содержащий имена обоих игроков.
    public UUID newMatch(PlayerRequestDto playerRequestDto1, PlayerRequestDto playerRequestDto2) {

        Player player1 = playerRepository.findByName(playerRequestDto1.name())
                .orElseGet(() -> playerRepository.save(playerMapper.toModel(playerRequestDto1)));

        Player player2 = playerRepository.findByName(playerRequestDto2.name())
                .orElseGet(() -> playerRepository.save(playerMapper.toModel(playerRequestDto2)));

        MatchScoreModel matchScoreModel = matchScoreModelMapper.toModel(player1, player2);

        return ongoingMatchService.save(matchScoreModel);
    }
}
