package com.stubedavd.view.controller.match;

import com.stubedavd.exception.ValidationException;
import com.stubedavd.model.match.service.OngoingMatchService;
import com.stubedavd.mapper.player.PlayerMapper;
import com.stubedavd.model.player.domain.PlayerDomain;
import com.stubedavd.model.player.entity.Player;
import com.stubedavd.model.player.repository.PlayerRepository;
import com.stubedavd.util.Validator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchController extends BaseController {
    private static final String JSP = "/WEB-INF/jsp/new-match.jsp";
    private static final String PLAYER_1_NAME = "player1Name";
    private static final String PLAYER_2_NAME = "player2Name";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String MATCH_SCORE_UUID = "/match-score?uuid=";
    private static final String PLAYER_NAME_IS_INVALID = "Player name is invalid";

    private OngoingMatchService ongoingMatchService;
    private PlayerMapper playerMapper;
    private PlayerRepository playerRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ongoingMatchService = getOngoingMatchService(config);
        playerMapper = getPlayerMapper(config);
        playerRepository = getPlayerRepository(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String player1Name = request.getParameter(PLAYER_1_NAME);
        String player2Name = request.getParameter(PLAYER_2_NAME);
        validatePlayers(request, response, player1Name, player2Name);

        player1Name = player1Name.trim();
        player2Name = player2Name.trim();
        String finalPlayer1Name = player1Name;
        String finalPlayer2Name = player2Name;
        Player player1 = playerRepository.findByName(player1Name)
                .orElseGet(() -> playerRepository.save(playerMapper.toEntity(finalPlayer1Name)));
        Player player2 = playerRepository.findByName(player2Name)
                .orElseGet(() -> playerRepository.save(playerMapper.toEntity(finalPlayer2Name)));
        PlayerDomain player1Domain = playerMapper.toDomain(player1);
        PlayerDomain player2Domain = playerMapper.toDomain(player2);

        UUID matchId = ongoingMatchService.save(player1Domain, player2Domain);

        response.sendRedirect(request.getContextPath() + MATCH_SCORE_UUID + matchId);
    }

    private void validatePlayers(
            HttpServletRequest request,
            HttpServletResponse response,
            String player1Name,
            String player2Name
    ) throws ServletException, IOException {
        try {
            Validator.validatePlayers(player1Name, player2Name);
        } catch (ValidationException e) {
            switch (e.getMessage()) {
                case Validator.PLAYER_NAMES_CANNOT_BE_NULL
                        -> request.setAttribute(ERROR_MESSAGE, Validator.PLAYER_NAMES_CANNOT_BE_NULL);
                case Validator.PLAYER_NAMES_CANNOT_BE_BLANK ->
                    request.setAttribute(ERROR_MESSAGE, Validator.PLAYER_NAMES_CANNOT_BE_BLANK);
                case Validator.PLAYER_1_VALIDATION_RULES ->
                    request.setAttribute(ERROR_MESSAGE, Validator.PLAYER_1_VALIDATION_RULES);
                case Validator.PLAYER_2_VALIDATION_RULES ->
                    request.setAttribute(ERROR_MESSAGE, Validator.PLAYER_2_VALIDATION_RULES);
                case Validator.PLAYER_1_NAME_IS_TOO_LONG ->
                    request.setAttribute(ERROR_MESSAGE, Validator.PLAYER_1_NAME_IS_TOO_LONG);
                case Validator.PLAYER_2_NAME_IS_TOO_LONG ->
                    request.setAttribute(ERROR_MESSAGE, Validator.PLAYER_2_NAME_IS_TOO_LONG);
                default -> request.setAttribute(ERROR_MESSAGE, PLAYER_NAME_IS_INVALID);
            }

            request.setAttribute(PLAYER_1_NAME, player1Name);
            request.setAttribute(PLAYER_2_NAME, player2Name);

            request.getRequestDispatcher(JSP).forward(request, response);
        }
    }
}
