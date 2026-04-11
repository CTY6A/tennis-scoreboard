package com.stubedavd.controller.servlet;

import com.stubedavd.dto.request.PlayerRequestDto;
import com.stubedavd.exception.NotFoundException;
import com.stubedavd.exception.ValidationException;
import com.stubedavd.listener.ContextListener;
import com.stubedavd.mapper.PlayerMapper;
import com.stubedavd.service.NewMatchService;
import com.stubedavd.util.Validator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchServlet extends BaseServlet {

    public static final String JSP = "/WEB-INF/jsp/new-match.jsp";

    private NewMatchService newMatchService;

    private PlayerMapper playerMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        newMatchService =
                (NewMatchService) config.getServletContext().getAttribute(ContextListener.NEW_MATCH_SERVICE);

        if (newMatchService == null) {

            throw new NotFoundException("New match service not found");
        }

        playerMapper =
                (PlayerMapper) config.getServletContext().getAttribute(ContextListener.PLAYER_MAPPER);

        if (playerMapper == null) {

            throw new NotFoundException("Player mapper not found");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String player1Name = request.getParameter("player1");
        String player2Name = request.getParameter("player2");

        try {

            Validator.validatePlayers(player1Name, player2Name);
        } catch (ValidationException e) {

            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher(JSP).forward(request, response);
        }

        player1Name = player1Name.trim();
        player2Name = player2Name.trim();

        PlayerRequestDto player1 = playerMapper.toRequestDto(player1Name);
        PlayerRequestDto player2 = playerMapper.toRequestDto(player2Name);

        UUID matchId = newMatchService.newMatch(player1, player2);

        response.sendRedirect(request.getContextPath() + "/match-score?uuid=" + matchId);
    }
}
