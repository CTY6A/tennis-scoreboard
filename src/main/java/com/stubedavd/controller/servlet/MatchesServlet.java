package com.stubedavd.controller.servlet;

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

@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {

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


        List<MatchResponseDto> matches;
        int pageCount;

        String playerName = request.getParameter("filter_by_player_name");

        if (playerName != null && !playerName.isBlank()) {

            playerName = playerName.trim();

            Validator.validatePlayerName(playerName);

            pageCount = (int) (matchesService.getCountByName(playerName) / PAGE_SIZE);

            if (pageNumber > pageCount - 1 && pageCount != 0) {

                pageNumber = pageCount - 1;
            }

            matches = matchesService.getByPlayerName(playerName, pageNumber, PAGE_SIZE);
        } else {

            pageCount = (int) (matchesService.getTotalCount() / PAGE_SIZE);

            if (pageNumber > pageCount - 1 && pageCount != 0) {

                pageNumber = pageCount - 1;
            }

            matches = matchesService.getPage(pageNumber, PAGE_SIZE);
        }

        request.setAttribute("matches", matches);
        request.setAttribute("playerName", playerName);
        request.setAttribute("pageCount", pageCount);
        request.setAttribute("pageNumber", pageNumber + 1 );

        request.getRequestDispatcher(JSP).forward(request, response);
    }
}
