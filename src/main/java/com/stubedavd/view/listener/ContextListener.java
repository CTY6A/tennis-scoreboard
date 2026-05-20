package com.stubedavd.view.listener;

import com.stubedavd.mapper.match.MatchMapper;
import com.stubedavd.mapper.match.MatchScoreModelMapper;
import com.stubedavd.model.match.repository.MatchRepository;
import com.stubedavd.model.match.service.*;
import com.stubedavd.model.match.service.impl.*;
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
        NewMatchService newMatchService = new NewMatchServiceImpl(
                ongoingMatchService,
                playerMapper,
                playerRepository
        );
        MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationServiceImpl();
        MatchScoreService matchScoreService = new MatchScoreServiceImpl(
                ongoingMatchService,
                matchScoreCalculationService,
                playerMapper,
                matchMapper,
                matchScoreModelMapper,
                matchRepository
        );
        MatchesService matchesService = new MatchesServiceImpl(matchMapper, matchRepository);

        servletContext.setAttribute(PlayerRepository.class.getSimpleName(), playerRepository);
        servletContext.setAttribute(MatchRepository.class.getSimpleName(), matchRepository);

        servletContext.setAttribute(PlayerMapper.class.getSimpleName(), playerMapper);
        servletContext.setAttribute(MatchMapper.class.getSimpleName(), matchMapper);

        servletContext.setAttribute(OngoingMatchService.class.getSimpleName(), ongoingMatchService);
        servletContext.setAttribute(NewMatchService.class.getSimpleName(), newMatchService);
        servletContext.setAttribute(MatchScoreService.class.getSimpleName(), matchScoreService);
        servletContext.setAttribute(MatchScoreCalculationService.class.getSimpleName(), matchScoreCalculationService);
        servletContext.setAttribute(MatchesService.class.getSimpleName(), matchesService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.close();
    }
}
