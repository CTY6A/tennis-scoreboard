package com.stubedavd.controller.servlet;

import com.stubedavd.dto.response.MatchResponseDto;
import com.stubedavd.entity.Match;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.listener.ContextListener;
import com.stubedavd.service.MatchScoreService;
import com.stubedavd.service.MatchesService;
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

        String playerName = request.getParameter("filter_by_player_name");
        request.setAttribute("playerName", playerName);
        System.out.println(playerName);

        List<MatchResponseDto> matches = matchesService.getPage(0, 5);

        request.setAttribute("matches", matches);

        request.getRequestDispatcher(JSP).forward(request, response);
    }
}
