package com.stubedavd.view.controller.match;

import com.stubedavd.exception.NotFoundException;
import com.stubedavd.mapper.player.PlayerMapper;
import com.stubedavd.model.match.service.FinishedMatchesPersistenceService;
import com.stubedavd.model.match.service.MatchScoreCalculationService;
import com.stubedavd.model.match.service.MatchesService;
import com.stubedavd.model.match.service.OngoingMatchService;
import com.stubedavd.model.player.repository.PlayerRepository;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;

public class BaseController extends HttpServlet {
    private static final String ONGOING_MATCH_SERVICE_NOT_FOUND = "Ongoing match service not found";
    private static final String PLAYER_MAPPER_NOT_FOUND = "Player mapper not found";
    private static final String PLAYER_REPOSITORY_NOT_FOUND = "Player repository not found";
    private static final String MATCHES_SERVICE_NOT_FOUND = "Matches service not found";
    private static final String FINISHED_MATCHES_PERSISTENCE_SERVICE_NOT_FOUND = "Finished Matches persistence service not found";
    private static final String MATCH_SCORE_CALCULATION_SERVICE_NOT_FOUND = "Match score calculation service not found";

    protected MatchesService getMatchesService(ServletConfig config) {
        MatchesService matchesService =
                (MatchesService) config.getServletContext().getAttribute(MatchesService.class.getSimpleName());

        if (matchesService == null) {
            throw new NotFoundException(MATCHES_SERVICE_NOT_FOUND);
        }
        return matchesService;
    }

    protected FinishedMatchesPersistenceService getFinishedMatchesPersistenceService(ServletConfig config) {
        FinishedMatchesPersistenceService finishedMatchesPersistenceService =
                (FinishedMatchesPersistenceService) config
                        .getServletContext()
                        .getAttribute(FinishedMatchesPersistenceService.class.getSimpleName());

        if (finishedMatchesPersistenceService == null) {
            throw new NotFoundException(FINISHED_MATCHES_PERSISTENCE_SERVICE_NOT_FOUND);
        }
        return finishedMatchesPersistenceService;
    }

    protected MatchScoreCalculationService getMatchScoreCalculationService(ServletConfig config) {
        MatchScoreCalculationService matchScoreCalculationService =
                (MatchScoreCalculationService) config
                        .getServletContext()
                        .getAttribute(MatchScoreCalculationService.class.getSimpleName());
        if (matchScoreCalculationService == null) {
            throw new NotFoundException(MATCH_SCORE_CALCULATION_SERVICE_NOT_FOUND);
        }
        return matchScoreCalculationService;
    }

    protected PlayerRepository getPlayerRepository(ServletConfig config) {
        PlayerRepository playerRepository =
                (PlayerRepository) config.getServletContext().getAttribute(PlayerRepository.class.getSimpleName());
        if (playerRepository == null) {
            throw new NotFoundException(PLAYER_REPOSITORY_NOT_FOUND);
        }
        return playerRepository;
    }

    protected PlayerMapper getPlayerMapper(ServletConfig config) {
        PlayerMapper playerMapper =
                (PlayerMapper) config.getServletContext().getAttribute(PlayerMapper.class.getSimpleName());
        if (playerMapper == null) {
            throw new NotFoundException(PLAYER_MAPPER_NOT_FOUND);
        }
        return playerMapper;
    }

    protected OngoingMatchService getOngoingMatchService(ServletConfig config) {
        OngoingMatchService ongoingMatchService =
                (OngoingMatchService) config
                        .getServletContext()
                        .getAttribute(OngoingMatchService.class.getSimpleName());
        if (ongoingMatchService == null) {
            throw new NotFoundException(ONGOING_MATCH_SERVICE_NOT_FOUND);
        }
        return ongoingMatchService;
    }
}
