package com.stubedavd.view.controller.match;

import com.stubedavd.exception.NotFoundException;
import com.stubedavd.mapper.match.MatchMapper;
import com.stubedavd.model.match.service.*;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;

public class BaseController extends HttpServlet {
    private static final String PLAYER_MAPPER_NOT_FOUND = "Player mapper not found";
    private static final String MATCHES_SERVICE_NOT_FOUND = "Matches service not found";
    public static final String MATCH_SCORE_SERVICE_NOT_FOUND = "Match score service not found";
    public static final String NEW_MATCH_SERVICE_NOT_FOUND = "New match service not found";

    protected MatchScoreService getMatchScoreService(ServletConfig config) {
        MatchScoreService matchScoreService =
                (MatchScoreService) config.getServletContext().getAttribute(MatchScoreService.class.getSimpleName());

        if (matchScoreService == null) {
            throw new NotFoundException(MATCH_SCORE_SERVICE_NOT_FOUND);
        }
        return matchScoreService;
    }

    protected MatchesService getMatchesService(ServletConfig config) {
        MatchesService matchesService =
                (MatchesService) config.getServletContext().getAttribute(MatchesService.class.getSimpleName());

        if (matchesService == null) {
            throw new NotFoundException(MATCHES_SERVICE_NOT_FOUND);
        }
        return matchesService;
    }

    protected NewMatchService getNewMatchService(ServletConfig config) {
        NewMatchService newMatchService =
                (NewMatchService) config.getServletContext().getAttribute(NewMatchService.class.getSimpleName());
        if (newMatchService == null) {
            throw new NotFoundException(NEW_MATCH_SERVICE_NOT_FOUND);
        }
        return newMatchService;
    }

    protected MatchMapper getMatchMapper(ServletConfig config) {
        MatchMapper matchMapper =
                (MatchMapper) config.getServletContext().getAttribute(MatchMapper.class.getSimpleName());
        if (matchMapper == null) {
            throw new NotFoundException(PLAYER_MAPPER_NOT_FOUND);
        }
        return matchMapper;
    }
}
