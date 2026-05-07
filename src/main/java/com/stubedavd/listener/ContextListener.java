package com.stubedavd.listener;

import com.stubedavd.match.mapper.MatchMapper;
import com.stubedavd.match.mapper.MatchScoreModelMapper;
import com.stubedavd.match.model.service.*;
import com.stubedavd.player.mapper.PlayerMapper;
import com.stubedavd.match.model.repository.MatchRepository;
import com.stubedavd.player.model.repository.PlayerRepository;
import com.stubedavd.util.HibernateUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    public static final String PLAYER_MAPPER = "playerMapper";
    public static final String MATCH_MAPPER = "matchMapper";

    public static final String PLAYER_REPOSITORY = "playerRepository";
    public static final String MATCH_REPOSITORY = "matchRepository";

    public static final String ONGOING_MATCH_SERVICE = "ongoingMatchService";
    public static final String NEW_MATCH_SERVICE = "newMatchService";
    public static final String MATCH_SCORE_CALCULATION_SERVICE = "matchScoreCalculationService";
    public static final String MATCHES_SERVICE = "matchesService";
    public static final String FINISHED_MATCHES_PERSISTENCE_SERVICE = "finishedMatchesPersistenceService";

    // Для помещения объектов в контекст можно использовать "естественные константы" — Bean.class.getSimpleName() или Bean.class.getName()
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        HibernateUtil.init();

        ServletContext servletContext = sce.getServletContext();

        PlayerMapper playerMapper = PlayerMapper.INSTANCE;
        MatchMapper matchMapper = MatchMapper.INSTANCE;
        MatchScoreModelMapper matchScoreModelMapper = MatchScoreModelMapper.INSTANCE;

        PlayerRepository playerRepository = new PlayerRepository();
        MatchRepository matchRepository = new MatchRepository();

        OngoingMatchService ongoingMatchService = new OngoingMatchService();
        NewMatchService newMatchService = new NewMatchService(
                ongoingMatchService,
                playerMapper,
                matchScoreModelMapper,
                playerRepository
        );
        MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
        MatchesService matchesService = new MatchesService(matchMapper, matchRepository);
        FinishedMatchesPersistenceService finishedMatchesPersistenceService =
                new FinishedMatchesPersistenceService(playerMapper, matchMapper, matchRepository);

        servletContext.setAttribute(PLAYER_REPOSITORY, playerRepository);
        servletContext.setAttribute(MATCH_REPOSITORY, matchRepository);

        servletContext.setAttribute(PLAYER_MAPPER, playerMapper);
        servletContext.setAttribute(MATCH_MAPPER, matchMapper);

        servletContext.setAttribute(ONGOING_MATCH_SERVICE, ongoingMatchService);
        servletContext.setAttribute(NEW_MATCH_SERVICE, newMatchService);
        servletContext.setAttribute(MATCH_SCORE_CALCULATION_SERVICE, matchScoreCalculationService);
        servletContext.setAttribute(MATCHES_SERVICE, matchesService);
        servletContext.setAttribute(FINISHED_MATCHES_PERSISTENCE_SERVICE, finishedMatchesPersistenceService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.close();
    }
}
