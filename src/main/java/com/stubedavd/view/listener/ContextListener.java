package com.stubedavd.view.listener;

import com.stubedavd.mapper.match.MatchMapper;
import com.stubedavd.mapper.match.MatchScoreModelMapper;
import com.stubedavd.model.match.repository.MatchRepository;
import com.stubedavd.model.match.service.FinishedMatchesPersistenceService;
import com.stubedavd.model.match.service.MatchScoreCalculationService;
import com.stubedavd.model.match.service.MatchesService;
import com.stubedavd.model.match.service.OngoingMatchService;
import com.stubedavd.model.match.service.impl.FinishedMatchesPersistenceServiceImpl;
import com.stubedavd.model.match.service.impl.MatchScoreCalculationServiceImpl;
import com.stubedavd.model.match.service.impl.MatchesServiceImpl;
import com.stubedavd.model.match.service.impl.OngoingMatchServiceImpl;
import com.stubedavd.mapper.player.PlayerMapper;
import com.stubedavd.model.match.repository.impl.MatchRepositoryImpl;
import com.stubedavd.model.player.repository.PlayerRepository;
import com.stubedavd.model.player.repository.impl.PlayerRepositoryImpl;
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
