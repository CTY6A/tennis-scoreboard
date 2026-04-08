package com.stubedavd.listener;

import com.stubedavd.mapper.MatchMapper;
import com.stubedavd.mapper.MatchScoreMapper;
import com.stubedavd.mapper.PlayerMapper;
import com.stubedavd.service.MatchScoreCalculationService;
import com.stubedavd.service.OngoingMatchService;
import com.stubedavd.service.impl.MatchScoreCalculationServiceImpl;
import com.stubedavd.service.impl.OngoingMatchServiceImpl;
import com.stubedavd.util.HibernateUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    public static final String MATCH_MAPPER = "matchMapper";
    public static final String MATCH_SCORE_MAPPER = "matchScoreMapper";
    public static final String PLAYER_MAPPER = "playerMapper";
    public static final String ONGOING_MATCH_SERVICE = "ongoingMatchService";
    public static final String MATCH_SCORE_CALCULATION_SERVICE = "matchScoreCalculationService";

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        HibernateUtil.init();

        ServletContext servletContext = sce.getServletContext();

        MatchMapper matchMapper = MatchMapper.INSTANCE;
        MatchScoreMapper matchScoreMapper = MatchScoreMapper.INSTANCE;
        PlayerMapper playerMapper = PlayerMapper.INSTANCE;

        OngoingMatchService ongoingMatchService =
                new OngoingMatchServiceImpl(matchMapper, matchScoreMapper, playerMapper);

        MatchScoreCalculationService matchScoreCalculationService =
                new MatchScoreCalculationServiceImpl();

        servletContext.setAttribute(MATCH_MAPPER, matchMapper);
        servletContext.setAttribute(MATCH_SCORE_MAPPER, matchScoreMapper);
        servletContext.setAttribute(PLAYER_MAPPER, playerMapper);

        servletContext.setAttribute(ONGOING_MATCH_SERVICE, ongoingMatchService);
        servletContext.setAttribute(MATCH_SCORE_CALCULATION_SERVICE, matchScoreCalculationService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.close();
    }
}
