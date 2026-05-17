package com.stubedavd.listener;

import com.stubedavd.match.mapper.MatchMapper;
import com.stubedavd.match.mapper.MatchScoreModelMapper;
import com.stubedavd.match.model.repository.MatchRepository;
import com.stubedavd.match.model.service.*;
import com.stubedavd.match.model.service.impl.FinishedMatchesPersistenceServiceImpl;
import com.stubedavd.match.model.service.impl.MatchScoreCalculationServiceImpl;
import com.stubedavd.match.model.service.impl.MatchesServiceImpl;
import com.stubedavd.match.model.service.impl.OngoingMatchServiceImpl;
import com.stubedavd.player.mapper.PlayerMapper;
import com.stubedavd.match.model.repository.impl.MatchRepositoryImpl;
import com.stubedavd.player.model.repository.PlayerRepository;
import com.stubedavd.player.model.repository.impl.PlayerRepositoryImpl;
import com.stubedavd.util.HibernateUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        HibernateUtil.init();

        ServletContext servletContext = sce.getServletContext();

        PlayerMapper playerMapper = PlayerMapper.INSTANCE;
        MatchMapper matchMapper = MatchMapper.INSTANCE;
        MatchScoreModelMapper matchScoreModelMapper = MatchScoreModelMapper.INSTANCE;

        PlayerRepository playerRepository = new PlayerRepositoryImpl(HibernateUtil.getSessionFactory());
        MatchRepository matchRepository = new MatchRepositoryImpl();

        OngoingMatchService ongoingMatchService = new OngoingMatchServiceImpl(matchScoreModelMapper);
        MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationServiceImpl();
        MatchesService matchesService = new MatchesServiceImpl(matchMapper, matchRepository);
        FinishedMatchesPersistenceService finishedMatchesPersistenceService =
                new FinishedMatchesPersistenceServiceImpl(playerMapper, matchMapper, matchRepository);

        servletContext.setAttribute(PlayerRepository.class.getSimpleName(), playerRepository);
        servletContext.setAttribute(MatchRepository.class.getSimpleName(), matchRepository);

        servletContext.setAttribute(PlayerMapper.class.getSimpleName(), playerMapper);
        servletContext.setAttribute(MatchMapper.class.getSimpleName(), matchMapper);

        servletContext.setAttribute(OngoingMatchService.class.getSimpleName(), ongoingMatchService);
        servletContext.setAttribute(MatchScoreCalculationService.class.getSimpleName(), matchScoreCalculationService);
        servletContext.setAttribute(MatchesService.class.getSimpleName(), matchesService);
        servletContext.setAttribute(
                FinishedMatchesPersistenceService.class.getSimpleName(),
                finishedMatchesPersistenceService
        );
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.close();
    }
}
