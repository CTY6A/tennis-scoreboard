package com.stubedavd.controller;

import com.stubedavd.dto.response.MatchResponseDto;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.listener.ContextListener;
import com.stubedavd.service.MatchesService;
import com.stubedavd.util.Validator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/matches")
public class MatchesController extends HttpServlet {

    public static final String JSP = "/WEB-INF/jsp/matches.jsp";

    public static final int PAGE_SIZE = 10;
    public static final int PAGE_NUMBER_0 = 0;

    private MatchesService matchesService;

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        matchesService =
                (MatchesService) config.getServletContext().getAttribute(ContextListener.MATCHES_SERVICE);

        if (matchesService == null) {
            throw new NotFoundException("Matches service not found");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<MatchResponseDto> matches;
        int pageNumber = getPageNumber(request);
        int pageCount;
        Long matchesCount;
        Optional<String> playerNameOptional = getPlayerFilter(request);

        if (playerNameOptional.isPresent()) {

            matchesCount = matchesService.getTotalCount(playerNameOptional.get());
        } else {

            matchesCount = matchesService.getTotalCount();
        }

        pageCount = (int) ((matchesCount + PAGE_SIZE - 1) / PAGE_SIZE);

        if (pageNumber > pageCount - 1 && pageCount != 0) {

            pageNumber = pageCount - 1;
        }

        if (playerNameOptional.isPresent()) {

            matches = matchesService.getPage(playerNameOptional.get(), pageNumber, PAGE_SIZE);
        } else {

            matches = matchesService.getPage(pageNumber, PAGE_SIZE);
        }

        request.setAttribute("matches", matches);
        request.setAttribute("playerName", playerNameOptional.orElse(""));
        request.setAttribute("pageCount", pageCount);
        request.setAttribute("pageNumber", pageNumber + 1 );
        request.setAttribute("matchesCount", matchesCount);
        request.setAttribute("matchesFrom", pageNumber * PAGE_SIZE);
        request.setAttribute("matchesTo", pageNumber * PAGE_SIZE + matches.size());

        request.getRequestDispatcher(JSP).forward(request, response);
    }

    private int getPageNumber(HttpServletRequest request) {

        int pageNumber = PAGE_NUMBER_0;
        String pageNumberString = request.getParameter("page");

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

    private Optional<String> getPlayerFilter(HttpServletRequest request) {

        String playerName = request.getParameter("filter_by_player_name");

        if (playerName != null && !playerName.isBlank()) {

            playerName = playerName.trim();
            Validator.validatePlayerName(playerName);

            return Optional.of(playerName);
        }

        return Optional.empty();
    }
}
