package com.stubedavd.controller;

import com.stubedavd.dto.OngoingMatchDto;
import com.stubedavd.entity.Player;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.listener.ContextListener;
import com.stubedavd.model.MatchScoreModel;
import com.stubedavd.service.FinishedMatchesPersistenceService;
import com.stubedavd.service.MatchScoreCalculationService;
import com.stubedavd.service.OngoingMatchService;
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

    public static final String MATCH_SCORE_JSP = "/WEB-INF/jsp/match-score.jsp";
    public static final String FINAL_MATCH_SCORE_JSP = "/WEB-INF/jsp/final-match-score.jsp";

    private OngoingMatchService ongoingMatchService;
    private MatchScoreCalculationService matchScoreCalculationService;
    private FinishedMatchesPersistenceService finishedMatchesPersistenceService;

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

        OngoingMatchDto ongoingMatchDto = ongoingMatchService.get(uuid);
        MatchScoreModel matchScoreModel = ongoingMatchDto.matchScoreModel();

        String player1PointsString = getPointsString(
                matchScoreModel,
                ongoingMatchDto.player1(),
                ongoingMatchDto.player2()
        );

        String player2PointsString = getPointsString(
                matchScoreModel,
                ongoingMatchDto.player2(),
                ongoingMatchDto.player1()
        );

        request.setAttribute("player1Id", ongoingMatchDto.player1().getId());
        request.setAttribute("player1Name", ongoingMatchDto.player1().getName());
        request.setAttribute("player1Sets", matchScoreModel.getSets().getScore(ongoingMatchDto.player1()));
        request.setAttribute("player1Games", matchScoreModel.getGames().getScore(ongoingMatchDto.player1()));
        request.setAttribute("player1Points", player1PointsString);

        request.setAttribute("player2Id", ongoingMatchDto.player2().getId());
        request.setAttribute("player2Name", ongoingMatchDto.player2().getName());
        request.setAttribute("player2Sets", matchScoreModel.getSets().getScore(ongoingMatchDto.player2()));
        request.setAttribute("player2Games", matchScoreModel.getGames().getScore(ongoingMatchDto.player2()));
        request.setAttribute("player2Points", player2PointsString);

        request.setAttribute("uuid", uuid);

        request.getRequestDispatcher(MATCH_SCORE_JSP).forward(request, response);
    }

    private String getPointsString(MatchScoreModel matchScoreModel, Player player1, Player player2) {

        int player1Points = matchScoreModel.getPoints().getScore(player1);
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
                default -> "0";
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

        OngoingMatchDto ongoingMatchDto = ongoingMatchService.get(uuid);
        MatchScoreModel matchScoreModel = ongoingMatchDto.matchScoreModel();

        if (matchScoreCalculationService.isMatchFinished(matchScoreModel)) {

            matchFinishedProcessing(request, response, uuid);
        } else {

            Player player = getPlayer(request, ongoingMatchDto);

            matchScoreCalculationService.pointWon(matchScoreModel, player);

            if (matchScoreCalculationService.isMatchFinished(matchScoreModel)) {

                matchFinishedProcessing(request, response, uuid);
            } else {

                response.sendRedirect(request.getContextPath() + "/match-score?uuid=" + uuid);
            }
        }
    }

    private Player getPlayer(HttpServletRequest request, OngoingMatchDto ongoingMatchDto) {

        String playerIdString = request.getParameter("playerId");
        Validator.validatePlayerId(playerIdString);
        Integer playerId = Integer.parseInt(playerIdString);

        if (playerId.equals(ongoingMatchDto.player1().getId())) {
            return ongoingMatchDto.player1();
        }

        if (playerId.equals(ongoingMatchDto.player2().getId())) {
            return ongoingMatchDto.player2();
        }

        throw new NotFoundException("Player with this id does not exist in this match score");
    }

    private void matchFinishedProcessing(HttpServletRequest request, HttpServletResponse response, UUID uuid)
            throws ServletException, IOException {

        OngoingMatchDto ongoingMatchDto = ongoingMatchService.get(uuid);
        MatchScoreModel matchScoreModel = ongoingMatchDto.matchScoreModel();

        finishedMatchesPersistenceService.recordMatch(ongoingMatchDto);
        ongoingMatchService.delete(uuid);

        request.setAttribute("player1Name", ongoingMatchDto.player1().getName());
        request.setAttribute("player1Score", matchScoreModel
                .getScore()
                .stream()
                .map(matchGame -> matchGame.getScore(ongoingMatchDto.player1()))
                .toList());

        request.setAttribute("player2Name", ongoingMatchDto.player2().getName());
        request.setAttribute("player2Score", matchScoreModel.getScore()
                .stream()
                .map(matchGame -> matchGame.getScore(ongoingMatchDto.player2()))
                .toList());

        request.getRequestDispatcher(FINAL_MATCH_SCORE_JSP).forward(request, response);
    }
}
