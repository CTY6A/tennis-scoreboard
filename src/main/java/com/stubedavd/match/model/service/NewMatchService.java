package com.stubedavd.match.model.service;

import com.stubedavd.match.mapper.MatchScoreModelMapper;
import com.stubedavd.match.model.domain.MatchScoreModel;
import com.stubedavd.match.model.service.impl.OngoingMatchServiceImpl;
import com.stubedavd.player.model.domain.PlayerDomain;
import com.stubedavd.player.model.dto.request.PlayerRequestDto;
import com.stubedavd.player.model.entity.Player;
import com.stubedavd.player.mapper.PlayerMapper;
import com.stubedavd.player.model.repository.PlayerRepository;

import java.util.UUID;

public class NewMatchService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Класс отвечает за создание объекта текущего матча (доменной модели).
        // При этом он способствует смешению слоёв — сам использует зависимость от DAO и передаёт JPA Entity в доменную модель.
        // (см. файл "Принцип разделения ответственности (Separation of Concerns).md" в этом же пакете)
        // Этому классу должна быть не нужна зависимость PlayerRepositoryImpl.
        // А также создание нового матча можно перенести в OngoingMatchServiceImpl и удалить этот сервис.

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
                .orElseGet(() -> playerRepository.save(playerMapper.toEntity(playerRequestDto1)));

        Player player2 = playerRepository.findByName(playerRequestDto2.name())
                .orElseGet(() -> playerRepository.save(playerMapper.toEntity(playerRequestDto2)));

        PlayerDomain player1Domain = playerMapper.toDomain(player1);
        PlayerDomain player2Domain = playerMapper.toDomain(player2);

        MatchScoreModel matchScoreModel = matchScoreModelMapper.toDomain(player1Domain, player2Domain);

        return ongoingMatchService.save(matchScoreModel);
    }
}
