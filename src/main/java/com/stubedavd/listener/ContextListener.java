package com.stubedavd.listener;

import com.stubedavd.mapper.PlayerMapper;
import com.stubedavd.repository.PlayerRepository;
import com.stubedavd.repository.impl.HibernatePlayerRepository;
import com.stubedavd.service.MatchScoreCalculationService;
import com.stubedavd.service.NewMatchService;
import com.stubedavd.service.OngoingMatchService;
import com.stubedavd.service.impl.MatchScoreCalculationServiceImpl;
import com.stubedavd.service.impl.NewMatchServiceImpl;
import com.stubedavd.service.impl.OngoingMatchServiceImpl;
import com.stubedavd.util.HibernateUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    public static final String PLAYER_MAPPER = "playerMapper";
    public static final String ONGOING_MATCH_SERVICE = "ongoingMatchService";
    public static final String MATCH_SCORE_CALCULATION_SERVICE = "matchScoreCalculationService";
    public static final String NEW_MATCH_SERVICE = "newMatchService";
    public static final String PLAYER_REPOSITORY = "playerRepository";

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        HibernateUtil.init();

        ServletContext servletContext = sce.getServletContext();

        PlayerMapper playerMapper = PlayerMapper.INSTANCE;

        PlayerRepository playerRepository = new HibernatePlayerRepository();

        OngoingMatchService ongoingMatchService = new OngoingMatchServiceImpl();
        NewMatchService newMatchService = new NewMatchServiceImpl(playerRepository, ongoingMatchService, playerMapper);
        MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationServiceImpl();

        servletContext.setAttribute(PLAYER_REPOSITORY, playerRepository);

        servletContext.setAttribute(PLAYER_MAPPER, playerMapper);

        servletContext.setAttribute(NEW_MATCH_SERVICE, newMatchService);
        servletContext.setAttribute(ONGOING_MATCH_SERVICE, ongoingMatchService);
        servletContext.setAttribute(MATCH_SCORE_CALCULATION_SERVICE, matchScoreCalculationService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.close();
    }
}
