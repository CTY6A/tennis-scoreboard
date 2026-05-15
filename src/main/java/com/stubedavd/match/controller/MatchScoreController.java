package com.stubedavd.match.controller;

import com.stubedavd.match.model.domain.score.value.RegularGameScoreValue;
import com.stubedavd.match.model.service.impl.OngoingMatchServiceImpl;
import com.stubedavd.player.model.domain.PlayerDomain;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.listener.ContextListener;
import com.stubedavd.match.model.domain.MatchScoreModel;
import com.stubedavd.match.model.service.FinishedMatchesPersistenceService;
import com.stubedavd.match.model.service.MatchScoreCalculationService;
import com.stubedavd.util.Validator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends HttpServlet {

    // Все повторяющиеся или важные строковые литералы лучше вынести в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    // TODO: Сервлет берёт на себя лишнюю ответственность — оркестрирует взаимодействие между несколькими сервисами и содержит бизнес-логику,
        // хотя его задача — только принимать HTTP-запросы и делегировать их обработку. Это нарушает принцип единственной ответственности (SRP)
        // и делает код сервлета более сложным и трудным для тестирования.
        // Сервлет должен быть "тонким контроллером", делегирующим всю бизнес-логику одному фасадному сервису.
        // (см. файл "Архитектурный анти-паттерн: "Толстый контроллер" (Fat Controller).md" в этом же пакете)

    // TODO: Сервлет работает с доменной моделью `MatchScoreModel` и получает из неё JPA Entity игроков (`Player`).
        // Это нарушает границы между слоями приложения и Принцип разделения ответственности
        // (см. файл "Принцип разделения ответственности (Separation of Concerns).md" в этом же пакете).
        // Сервлет не должен работать с доменными моделями и JPA сущностями и знать о существовании класса `Player` — ему это не нужно для выполнения его задачи.

    public static final String MATCH_SCORE_JSP = "/WEB-INF/jsp/match-score.jsp";
    public static final String FINAL_MATCH_SCORE_JSP = "/WEB-INF/jsp/final-match-score.jsp";

    private OngoingMatchServiceImpl ongoingMatchService;
    private MatchScoreCalculationService matchScoreCalculationService;
    private FinishedMatchesPersistenceService finishedMatchesPersistenceService;

    // Для получения объектов из контекста можно использовать "естественные константы" — Service.class.getSimpleName() или Service.class.getName()
    // Логику получения бина и проверку его на null можно вынести в базовый контроллер в специальный метод, чтобы она не повторялась по нескольку раз в каждом сервлете.
    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        ongoingMatchService =
                (OngoingMatchServiceImpl) config.getServletContext().getAttribute(ContextListener.ONGOING_MATCH_SERVICE);

        if (ongoingMatchService == null) {
            throw new NotFoundException("Ongoing Match service not found");
        }

        matchScoreCalculationService =
                (MatchScoreCalculationService) config
                        .getServletContext()
                        .getAttribute(ContextListener.MATCH_SCORE_CALCULATION_SERVICE);

        if (matchScoreCalculationService == null) {
            throw new NotFoundException("Match score calculation service not found");
        }

        finishedMatchesPersistenceService =
                (FinishedMatchesPersistenceService) config
                        .getServletContext()
                        .getAttribute(ContextListener.FINISHED_MATCHES_PERSISTENCE_SERVICE);

        if (finishedMatchesPersistenceService == null) {
            throw new NotFoundException("Finished Matches persistence service not found");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UUID uuid = getUuid(request);

        // Сервлет не должен работать с доменной моделью (особенно которая содержит JPA Entity)
        MatchScoreModel matchScoreModel = ongoingMatchService.get(uuid);

        String player1PointsString = getPointsString(
                matchScoreModel,
                matchScoreModel.getPlayer1()
        );

        String player2PointsString = getPointsString(
                matchScoreModel,
                matchScoreModel.getPlayer2()
        );

        // Вместо передачи данных во View по частям, лучше создать специальный DTO
        request.setAttribute("player1Id", matchScoreModel.getPlayer1().id());
        request.setAttribute("player1Name", matchScoreModel.getPlayer1().name());
        request.setAttribute("player1Sets", matchScoreModel.getMatchScore().getScore(matchScoreModel.getPlayer1()));
        request.setAttribute("player1Games", matchScoreModel.getSetScore().getScore(matchScoreModel.getPlayer1()));
        request.setAttribute("player1Points", player1PointsString);

        // Вместо передачи данных во View по частям, лучше создать специальный DTO
        request.setAttribute("player2Id", matchScoreModel.getPlayer2().id());
        request.setAttribute("player2Name", matchScoreModel.getPlayer2().name());
        request.setAttribute("player2Sets", matchScoreModel.getMatchScore().getScore(matchScoreModel.getPlayer2()));
        request.setAttribute("player2Games", matchScoreModel.getSetScore().getScore(matchScoreModel.getPlayer2()));
        request.setAttribute("player2Points", player2PointsString);

        request.setAttribute("uuid", uuid);

        request.getRequestDispatcher(MATCH_SCORE_JSP).forward(request, response);
    }

    // Ответственность за подготовку счёта для отображения лучше вынести в специальный класс-маппер.
        // Соблюдение SRP в сервлете будет строже, если он не будет этим заниматься.
    private String getPointsString(MatchScoreModel matchScoreModel, PlayerDomain player1) {

        if (matchScoreModel.getSetScore().isTieBreak()) {
            return String.valueOf(matchScoreModel.getTiebreakScore().getScore(player1));
        }

        RegularGameScoreValue player1Points = matchScoreModel.getRegularGameScore().getScore(player1);

        return player1Points.toString();
    }

    private UUID getUuid(HttpServletRequest request) {

        String uuidString = request.getParameter("uuid");
        Validator.validateUuid(uuidString);
        return UUID.fromString(uuidString);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        UUID uuid = getUuid(request);

        // Сервлет не должен работать с доменной моделью (особенно которая содержит JPA Entity)
        MatchScoreModel matchScoreModel = ongoingMatchService.get(uuid);

        if (matchScoreCalculationService.isMatchFinished(matchScoreModel)) {

            matchFinishedProcessing(request, response, uuid);
        } else {

            // Сервлет не должен работать с JPA Entity
            PlayerDomain player = getPlayer(request, matchScoreModel);

            matchScoreCalculationService.pointWon(matchScoreModel, player);

            if (matchScoreCalculationService.isMatchFinished(matchScoreModel)) {

                matchFinishedProcessing(request, response, uuid);
            } else {

                response.sendRedirect(request.getContextPath() + "/match-score?uuid=" + uuid);
            }
        }
    }

    // Сервлет не должен работать с JPA Entity
    private PlayerDomain getPlayer(HttpServletRequest request, MatchScoreModel matchScoreModel) {

        String playerIdString = request.getParameter("playerId");

        // Validator лучше внедрять через метод init(), а не обращать к нему напрямую из этого метода
        Validator.validatePlayerId(playerIdString);
        Long playerId = Long.parseLong(playerIdString);

        if (playerId.equals(matchScoreModel.getPlayer1().id())) {
            return matchScoreModel.getPlayer1();
        }

        if (playerId.equals(matchScoreModel.getPlayer2().id())) {
            return matchScoreModel.getPlayer2();
        }

        throw new NotFoundException("Player with this id does not exist in this match score");
    }

    // Сервлет не должен заниматься бизнес-логикой. Это обязанность сервисного слоя.
    private void matchFinishedProcessing(HttpServletRequest request, HttpServletResponse response, UUID uuid)
            throws ServletException, IOException {

        MatchScoreModel matchScoreModel = ongoingMatchService.get(uuid);

        finishedMatchesPersistenceService.recordMatch(matchScoreModel);
        ongoingMatchService.delete(uuid);

        request.setAttribute("player1Name", matchScoreModel.getPlayer1().name());
        request.setAttribute("player1Score", matchScoreModel
                .getProtocol()
                .stream()
                .map(matchGame -> matchGame.getScore(matchScoreModel.getPlayer1()))
                .toList());

        request.setAttribute("player2Name", matchScoreModel.getPlayer2().name());
        request.setAttribute("player2Score", matchScoreModel
                .getProtocol()
                .stream()
                .map(matchGame -> matchGame.getScore(matchScoreModel.getPlayer2()))
                .toList());

        request.getRequestDispatcher(FINAL_MATCH_SCORE_JSP).forward(request, response);
    }
}
