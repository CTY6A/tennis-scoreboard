package com.stubedavd.view.controller.match;

import com.stubedavd.model.match.dto.response.FinalScoreResponseDto;
import com.stubedavd.model.match.dto.response.MatchScoreResponseDto;
import com.stubedavd.model.match.service.MatchScoreService;
import com.stubedavd.model.match.service.OngoingMatchService;
import com.stubedavd.util.Validator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends BaseController {
    // TODO: Сервлет берёт на себя лишнюю ответственность — оркестрирует взаимодействие между несколькими сервисами и содержит бизнес-логику,
        // хотя его задача — только принимать HTTP-запросы и делегировать их обработку. Это нарушает принцип единственной ответственности (SRP)
        // и делает код сервлета более сложным и трудным для тестирования.
        // Сервлет должен быть "тонким контроллером", делегирующим всю бизнес-логику одному фасадному сервису.
        // (см. файл "Архитектурный анти-паттерн: "Толстый контроллер" (Fat Controller).md" в этом же пакете)

    // TODO: Сервлет работает с доменной моделью `MatchScoreModel` и получает из неё JPA Entity игроков (`Player`).
        // Это нарушает границы между слоями приложения и Принцип разделения ответственности
        // (см. файл "Принцип разделения ответственности (Separation of Concerns).md" в этом же пакете).
        // Сервлет не должен работать с доменными моделями и JPA сущностями и знать о существовании класса `Player` — ему это не нужно для выполнения его задачи.

    private static final String MATCH_SCORE_JSP = "/WEB-INF/jsp/match-score.jsp";
    private static final String FINAL_MATCH_SCORE_JSP = "/WEB-INF/jsp/final-match-score.jsp";
    private static final String UUID = "uuid";
    private static final String MATCH_SCORE_UUID = "/match-score?uuid=";
    private static final String PLAYER_ID = "playerId";
    public static final String MATCH_SCORE = "matchScore";

    private MatchScoreService matchScoreService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        matchScoreService = getMatchScoreService(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UUID uuid = getUuid(request);
        MatchScoreResponseDto matchScoreResponseDto = matchScoreService.getScore(uuid);
        request.setAttribute(MATCH_SCORE, matchScoreResponseDto);
        request.setAttribute(UUID, uuid);
        request.getRequestDispatcher(MATCH_SCORE_JSP).forward(request, response);
    }

    private UUID getUuid(HttpServletRequest request) {
        String uuidString = request.getParameter(UUID);
        Validator.validateUuid(uuidString);
        return java.util.UUID.fromString(uuidString);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        UUID uuid = getUuid(request);

        if (matchScoreService.isMatchFinished(uuid)) {
            FinalScoreResponseDto finalScoreResponseDto = matchScoreService.recordMatch(uuid);
            request.setAttribute(MATCH_SCORE, finalScoreResponseDto);
            request.getRequestDispatcher(FINAL_MATCH_SCORE_JSP).forward(request, response);
        } else {
            String playerIdString = request.getParameter(PLAYER_ID);
            Validator.validatePlayerId(playerIdString);
            Long playerId = Long.parseLong(playerIdString);

            matchScoreService.pointWon(uuid, playerId);
            if (matchScoreService.isMatchFinished(uuid)) {
                FinalScoreResponseDto finalScoreResponseDto = matchScoreService.recordMatch(uuid);
                request.setAttribute(MATCH_SCORE, finalScoreResponseDto);
                request.getRequestDispatcher(FINAL_MATCH_SCORE_JSP).forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + MATCH_SCORE_UUID + uuid);
            }
        }
    }
}
