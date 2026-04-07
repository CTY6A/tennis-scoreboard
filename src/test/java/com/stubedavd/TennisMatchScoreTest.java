package com.stubedavd;

import com.stubedavd.dto.PlayerDto;
import com.stubedavd.exception.BusinessException;
import com.stubedavd.model.MatchScoreModel;
import com.stubedavd.service.MatchScoreCalculationService;
import com.stubedavd.service.impl.MatchScoreCalculationServiceImpl;
import org.junit.jupiter.api.*;

public class TennisMatchScoreTest {

    private MatchScoreCalculationService matchScoreCalculationService;
    private MatchScoreModel matchScoreModel;
    private PlayerDto playerDto1;
    private PlayerDto playerDto2;

    @BeforeEach
    void setUpBeforeClass() {

        matchScoreCalculationService = new MatchScoreCalculationServiceImpl();
        playerDto1 = new PlayerDto("Nadal");
        playerDto2 = new PlayerDto("Federer");
        matchScoreModel = new MatchScoreModel(playerDto1, playerDto2);
    }

    @Test
    void testInitialState() {

        Assertions.assertFalse(matchScoreCalculationService.isMatchFinished(matchScoreModel));
    }

    @Test
    void testGameWithoutDeuce() {

        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);

        Assertions.assertEquals(1, matchScoreModel.getGames().get(playerDto1));
    }

    @Test
    void testDeuceAndAdvantage() {

        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto2);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto2);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto2);


        Assertions.assertEquals(3, matchScoreModel.getPoints().get(playerDto1));
        Assertions.assertEquals(3, matchScoreModel.getPoints().get(playerDto2));

        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto2);

        Assertions.assertEquals(
                matchScoreModel.getPoints().get(playerDto1),
                matchScoreModel.getPoints().get(playerDto2)
        );

        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);

        Assertions.assertEquals(1, matchScoreModel.getGames().get(playerDto1));
    }

    @Test
    void testSetWonWithoutTiebreak() {

        addGames(playerDto1, 6);
        addGames(playerDto2, 4);

        Assertions.assertEquals(1, matchScoreModel.getSets().get(playerDto1));
        Assertions.assertFalse(matchScoreCalculationService.isMatchFinished(matchScoreModel));
    }

    private void addGames(PlayerDto playerDto, int gamesCount) {

        for (int i = 0; i < gamesCount; i++) {
            for (int j = 0; j < 4; j++) {
                matchScoreCalculationService.pointWon(matchScoreModel, playerDto);
            }
        }
    }

    @Test
    void testTiebreak() {

        addGames(playerDto1, 5);
        addGames(playerDto2, 6);
        addGames(playerDto1, 1);

        Assertions.assertEquals(6, matchScoreModel.getGames().get(playerDto1));
        Assertions.assertEquals(6, matchScoreModel.getGames().get(playerDto2));

        for (int i = 0; i < 5; i++) {
            matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        }
        for (int i = 0; i < 5; i++) {
            matchScoreCalculationService.pointWon(matchScoreModel, playerDto2);
        }
        for (int i = 0; i < 2; i++) {
            matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        }

        Assertions.assertEquals(1, matchScoreModel.getSets().get(playerDto1));
    }

    @Test
    void testMatchWon() {

        addGames(playerDto1, 6);
        addGames(playerDto2, 0);

        Assertions.assertEquals(1, matchScoreModel.getSets().get(playerDto1));

        addGames(playerDto1, 6);
        addGames(playerDto2, 0);

        Assertions.assertTrue(matchScoreCalculationService.isMatchFinished(matchScoreModel));
        Assertions.assertEquals(2, matchScoreModel.getSets().get(playerDto1));

        try {
            matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        } catch (BusinessException ignored) {}

        Assertions.assertEquals(2, matchScoreModel.getSets().get(playerDto1));
    }

    @Test
    void testSetWon7_5() {

        addGames(playerDto1, 5);
        addGames(playerDto2, 5);
        addGames(playerDto1, 2);

        Assertions.assertEquals(1, matchScoreModel.getSets().get(playerDto1));
    }

    @Test
    void testPointWon40_40() {

        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);

        matchScoreCalculationService.pointWon(matchScoreModel, playerDto2);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto2);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto2);

        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);

        Assertions.assertEquals(0, matchScoreModel.getGames().get(playerDto1));
    }

    @Test
    void testPointWon40_0() {

        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);
        matchScoreCalculationService.pointWon(matchScoreModel, playerDto1);

        Assertions.assertEquals(1, matchScoreModel.getGames().get(playerDto1));
    }
}
