package com.stubedavd.match.controller;

import com.stubedavd.player.mapper.PlayerMapper;
import com.stubedavd.player.model.domain.PlayerDomain;
import com.stubedavd.player.model.entity.Player;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.listener.ContextListener;
import com.stubedavd.match.model.domain.MatchScoreModel;
import com.stubedavd.match.model.service.FinishedMatchesPersistenceService;
import com.stubedavd.match.model.service.MatchScoreCalculationService;
import com.stubedavd.match.model.service.OngoingMatchService;
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

    private OngoingMatchService ongoingMatchService;
    private MatchScoreCalculationService matchScoreCalculationService;
    private FinishedMatchesPersistenceService finishedMatchesPersistenceService;

    // Для получения объектов из контекста можно использовать "естественные константы" — Service.class.getSimpleName() или Service.class.getName()
    // Логику получения бина и проверку его на null можно вынести в базовый контроллер в специальный метод, чтобы она не повторялась по нескольку раз в каждом сервлете.
    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        ongoingMatchService =
                (OngoingMatchService) config.getServletContext().getAttribute(ContextListener.ONGOING_MATCH_SERVICE);

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
                matchScoreModel.getPlayer1(),
                matchScoreModel.getPlayer2()
        );

        String player2PointsString = getPointsString(
                matchScoreModel,
                matchScoreModel.getPlayer2(),
                matchScoreModel.getPlayer1()
        );

        // Вместо передачи данных во View по частям, лучше создать специальный DTO
        request.setAttribute("player1Id", matchScoreModel.getPlayer1().getId());
        request.setAttribute("player1Name", matchScoreModel.getPlayer1().getName());
        request.setAttribute("player1Sets", matchScoreModel.getSets().getScore(matchScoreModel.getPlayer1()));
        request.setAttribute("player1Games", matchScoreModel.getGames().getScore(matchScoreModel.getPlayer1()));
        request.setAttribute("player1Points", player1PointsString);

        // Вместо передачи данных во View по частям, лучше создать специальный DTO
        request.setAttribute("player2Id", matchScoreModel.getPlayer2().getId());
        request.setAttribute("player2Name", matchScoreModel.getPlayer2().getName());
        request.setAttribute("player2Sets", matchScoreModel.getSets().getScore(matchScoreModel.getPlayer2()));
        request.setAttribute("player2Games", matchScoreModel.getGames().getScore(matchScoreModel.getPlayer2()));
        request.setAttribute("player2Points", player2PointsString);

        request.setAttribute("uuid", uuid);

        request.getRequestDispatcher(MATCH_SCORE_JSP).forward(request, response);
    }

    // Ответственность за подготовку счёта для отображения лучше вынести в специальный класс-маппер.
        // Соблюдение SRP в сервлете будет строже, если он не будет этим заниматься.
    private String getPointsString(MatchScoreModel matchScoreModel, PlayerDomain player1, PlayerDomain player2) {

        int player1Points = matchScoreModel.getPoints().getScore(player1);

        if (matchScoreModel.getGames().isTieBreak()) {
            return String.valueOf(player1Points);
        }

        int player2Points = matchScoreModel.getPoints().getScore(player2);

        if (player1Points >= 4 || player2Points >= 4) {

            if (player1Points > player2Points) {

                return "AD";
            } else {

                return "40";
            }
        } else {

            return switch (player1Points) {
                case 1 -> "15";
                case 2 -> "30";
                case 3 -> "40";
                default -> "0"; // По умолчанию не должен возвращаться 0. Здесь лучше выбрасывать исключение.
            };
        }
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

        if (playerId.equals(matchScoreModel.getPlayer1().getId())) {
            return matchScoreModel.getPlayer1();
        }

        if (playerId.equals(matchScoreModel.getPlayer2().getId())) {
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

        request.setAttribute("player1Name", matchScoreModel.getPlayer1().getName());
        request.setAttribute("player1Score", matchScoreModel
                .getScore()
                .stream()
                .map(matchGame -> matchGame.getScore(matchScoreModel.getPlayer1()))
                .toList());

        request.setAttribute("player2Name", matchScoreModel.getPlayer2().getName());
        request.setAttribute("player2Score", matchScoreModel.getScore()
                .stream()
                .map(matchGame -> matchGame.getScore(matchScoreModel.getPlayer2()))
                .toList());

        request.getRequestDispatcher(FINAL_MATCH_SCORE_JSP).forward(request, response);
    }
}
