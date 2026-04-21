package com.stubedavd.listener;

import com.stubedavd.mapper.MatchMapper;
import com.stubedavd.mapper.MatchScoreMapper;
import com.stubedavd.mapper.PlayerMapper;
import com.stubedavd.mapper.PlayerScoreMapper;
import com.stubedavd.repository.MatchRepository;
import com.stubedavd.repository.PlayerRepository;
import com.stubedavd.service.MatchScoreCalculationService;
import com.stubedavd.service.MatchScoreService;
import com.stubedavd.service.MatchesService;
import com.stubedavd.service.NewMatchService;
import com.stubedavd.service.OngoingMatchService;
import com.stubedavd.util.HibernateUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    public static final String NEW_MATCH_SERVICE = "newMatchService";
    public static final String MATCH_SCORE_SERVICE = "matchScoreService";
    public static final String ONGOING_MATCH_SERVICE = "ongoingMatchService";
    public static final String MATCH_SCORE_CALCULATION_SERVICE = "matchScoreCalculationService";
    public static final String MATCHES_SERVICE = "matchesService";
    public static final String PLAYER_REPOSITORY = "playerRepository";
    public static final String MATCH_REPOSITORY = "matchRepository";
    public static final String PLAYER_MAPPER = "playerMapper";
    public static final String MATCH_MAPPER = "matchMapper";
    public static final String MATCH_SCORE_MAPPER = "matchScoreMapper";
    public static final String PLAYER_SCORE_MAPPER = "playerScoreMapper";

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        HibernateUtil.init();

        ServletContext servletContext = sce.getServletContext();

        PlayerMapper playerMapper = PlayerMapper.INSTANCE;
        MatchScoreMapper matchScoreMapper = MatchScoreMapper.INSTANCE;
        PlayerScoreMapper playerScoreMapper = PlayerScoreMapper.INSTANCE;
        MatchMapper matchMapper = MatchMapper.INSTANCE;

        PlayerRepository playerRepository = new PlayerRepository();
        MatchRepository matchRepository = new MatchRepository();

        OngoingMatchService ongoingMatchService = new OngoingMatchService();
        NewMatchService newMatchService = new NewMatchService(playerRepository, ongoingMatchService, playerMapper);
        MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
        MatchScoreService matchScoreService = new MatchScoreService(
                ongoingMatchService,
                matchScoreCalculationService,
                matchMapper,
                matchScoreMapper,
                playerScoreMapper,
                matchRepository
        );
        MatchesService matchesService = new MatchesService(matchMapper, matchRepository);

        servletContext.setAttribute(PLAYER_REPOSITORY, playerRepository);
        servletContext.setAttribute(MATCH_REPOSITORY, matchRepository);

        servletContext.setAttribute(PLAYER_MAPPER, playerMapper);
        servletContext.setAttribute(MATCH_SCORE_MAPPER, matchScoreMapper);
        servletContext.setAttribute(PLAYER_SCORE_MAPPER, playerScoreMapper);
        servletContext.setAttribute(MATCH_MAPPER, matchMapper);

        servletContext.setAttribute(NEW_MATCH_SERVICE, newMatchService);
        servletContext.setAttribute(ONGOING_MATCH_SERVICE, ongoingMatchService);
        servletContext.setAttribute(MATCH_SCORE_CALCULATION_SERVICE, matchScoreCalculationService);
        servletContext.setAttribute(MATCH_SCORE_SERVICE, matchScoreService);
        servletContext.setAttribute(MATCHES_SERVICE, matchesService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.close();
    }
}
