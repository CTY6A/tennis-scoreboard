package com.stubedavd.servlet;

import com.stubedavd.dto.response.FinalScoreResponseDto;
import com.stubedavd.dto.response.MatchScoreResponseDto;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.listener.ContextListener;
import com.stubedavd.service.MatchScoreService;
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
public class MatchScoreServlet extends HttpServlet {

    public static final String MATCH_SCORE_JSP = "/WEB-INF/jsp/match-score.jsp";
    public static final String FINAL_MATCH_SCORE_JSP = "/WEB-INF/jsp/final-match-score.jsp";

    private MatchScoreService matchScoreService;

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        matchScoreService =
                (MatchScoreService) config.getServletContext().getAttribute(ContextListener.MATCH_SCORE_SERVICE);

        if (matchScoreService == null) {

            throw new NotFoundException("Match score service not found");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uuidString = request.getParameter("uuid");

        Validator.validateUuid(uuidString);

        UUID uuid = UUID.fromString(uuidString);

        MatchScoreResponseDto matchScoreResponseDto = matchScoreService.getMatchScore(uuid);

        request.setAttribute("player1Id", matchScoreResponseDto.playerScoreResponseDto1().id());
        request.setAttribute("player1Name", matchScoreResponseDto.playerScoreResponseDto1().name());
        request.setAttribute("player1Sets", matchScoreResponseDto.playerScoreResponseDto1().sets());
        request.setAttribute("player1Games", matchScoreResponseDto.playerScoreResponseDto1().games());
        request.setAttribute("player1Points", matchScoreResponseDto.playerScoreResponseDto1().points());

        request.setAttribute("player2Id", matchScoreResponseDto.playerScoreResponseDto2().id());
        request.setAttribute("player2Name", matchScoreResponseDto.playerScoreResponseDto2().name());
        request.setAttribute("player2Sets", matchScoreResponseDto.playerScoreResponseDto2().sets());
        request.setAttribute("player2Games", matchScoreResponseDto.playerScoreResponseDto2().games());
        request.setAttribute("player2Points", matchScoreResponseDto.playerScoreResponseDto2().points());

        request.setAttribute("uuid", uuid);

        request.getRequestDispatcher(MATCH_SCORE_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String uuidString = request.getParameter("uuid");

        Validator.validateUuid(uuidString);

        UUID uuid = UUID.fromString(uuidString);

        if (matchScoreService.isMatchFinished(uuid)) {

            matchFinishedProcessing(request, response, uuid);
        } else {

            String playerIdString = request.getParameter("playerId");

            Validator.validatePlayerId(playerIdString);

            Integer playerId = Integer.parseInt(playerIdString);

            matchScoreService.playerScore(uuid, playerId);

            if (matchScoreService.isMatchFinished(uuid)) {

                matchFinishedProcessing(request, response, uuid);
            } else {

                response.sendRedirect(request.getContextPath() + "/match-score?uuid=" + uuidString);
            }
        }
    }

    private void matchFinishedProcessing(HttpServletRequest request, HttpServletResponse response, UUID uuid) throws ServletException, IOException {

        FinalScoreResponseDto finalScoreResponseDto = matchScoreService.getFinalScore(uuid);

        matchScoreService.recordMatch(uuid);

        request.setAttribute("player1Name", finalScoreResponseDto.player1FinalScoreResponseDto().name());
        request.setAttribute("player1Score", finalScoreResponseDto.player1FinalScoreResponseDto().score());

        request.setAttribute("player2Name", finalScoreResponseDto.player2FinalScoreResponseDto().name());
        request.setAttribute("player2Score", finalScoreResponseDto.player2FinalScoreResponseDto().score());

        request.getRequestDispatcher(FINAL_MATCH_SCORE_JSP).forward(request, response);
    }
}
