package com.stubedavd.view.controller.match;

import com.stubedavd.model.match.dto.response.MatchesResponseDto;
import com.stubedavd.model.match.service.MatchesService;
import com.stubedavd.util.Validator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/matches")
public class MatchesController extends BaseController {
    private static final int PAGE_NUMBER_0 = 0;
    private static final String JSP = "/WEB-INF/jsp/matches.jsp";
    private static final String FILTER_BY_PLAYER_NAME = "filter_by_player_name";
    private static final String PAGE = "page";

    private MatchesService matchesService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        matchesService = getMatchesService(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String playerName = getPlayerFilter(request);
        int pageNumber = getPageNumber(request);
        MatchesResponseDto matchesResponseDto = matchesService.getMatches(playerName, pageNumber);
        request.setAttribute("matches", matchesResponseDto);
        request.getRequestDispatcher(JSP).forward(request, response);
    }


    private String getPlayerFilter(HttpServletRequest request) {
        String playerName = request.getParameter(FILTER_BY_PLAYER_NAME);
        if (playerName != null && !playerName.isBlank()) {
            playerName = playerName.trim();
            Validator.validatePlayerName(playerName);
            return playerName;
        }
        return "";
    }

    private int getPageNumber(HttpServletRequest request) {
        int pageNumber = PAGE_NUMBER_0;
        String pageNumberString = request.getParameter(PAGE);
        if (pageNumberString != null && !pageNumberString.isBlank()) {
            pageNumberString = pageNumberString.trim();
            Validator.validatePageNumber(pageNumberString);
            pageNumber = Integer.parseInt(pageNumberString);
            pageNumber--;
            if (pageNumber < PAGE_NUMBER_0) {
                pageNumber = PAGE_NUMBER_0;
            }
        }
        return pageNumber;
    }
}
