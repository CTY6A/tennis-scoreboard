package com.stubedavd;

import com.stubedavd.entity.Player;
import com.stubedavd.exception.BusinessException;
import com.stubedavd.model.MatchScoreModel;
import com.stubedavd.service.MatchScoreCalculationService;
import org.junit.jupiter.api.*;

public class TennisMatchScoreTest {

    private MatchScoreCalculationService matchScoreCalculationService;
    private MatchScoreModel matchScoreModel;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUpBeforeClass() {

        matchScoreCalculationService = new MatchScoreCalculationService();

        player1 = new Player();
        player1.setName("Nadal");

        player2 = new Player();
        player2.setName("Nadal");

        matchScoreModel = new MatchScoreModel(player1, player2);
    }

    @Test
    void testInitialState() {

        Assertions.assertFalse(matchScoreCalculationService.isMatchFinished(matchScoreModel));
    }

    @Test
    void testGameWithoutDeuce() {

        matchScoreCalculationService.pointWon(matchScoreModel, player1);
        matchScoreCalculationService.pointWon(matchScoreModel, player1);
        matchScoreCalculationService.pointWon(matchScoreModel, player1);
        matchScoreCalculationService.pointWon(matchScoreModel, player1);

        Assertions.assertEquals(1, matchScoreModel.getGames().get(player1));
    }

    @Test
    void testDeuceAndAdvantage() {

        matchScoreCalculationService.pointWon(matchScoreModel, player1);
        matchScoreCalculationService.pointWon(matchScoreModel, player2);
        matchScoreCalculationService.pointWon(matchScoreModel, player1);
        matchScoreCalculationService.pointWon(matchScoreModel, player2);
        matchScoreCalculationService.pointWon(matchScoreModel, player1);
        matchScoreCalculationService.pointWon(matchScoreModel, player2);


        Assertions.assertEquals(3, matchScoreModel.getPoints().get(player1));
        Assertions.assertEquals(3, matchScoreModel.getPoints().get(player2));

        matchScoreCalculationService.pointWon(matchScoreModel, player1);
        matchScoreCalculationService.pointWon(matchScoreModel, player2);

        Assertions.assertEquals(
                matchScoreModel.getPoints().get(player1),
                matchScoreModel.getPoints().get(player2)
        );

        matchScoreCalculationService.pointWon(matchScoreModel, player1);
        matchScoreCalculationService.pointWon(matchScoreModel, player1);

        Assertions.assertEquals(1, matchScoreModel.getGames().get(player1));
    }

    @Test
    void testSetWonWithoutTiebreak() {

        addGames(player1, 6);
        addGames(player2, 4);

        Assertions.assertEquals(1, matchScoreModel.getSets().get(player1));
        Assertions.assertFalse(matchScoreCalculationService.isMatchFinished(matchScoreModel));
    }

    private void addGames(Player player, int gamesCount) {

        for (int i = 0; i < gamesCount; i++) {
            for (int j = 0; j < 4; j++) {
                matchScoreCalculationService.pointWon(matchScoreModel, player);
            }
        }
    }

    @Test
    void testTiebreak() {

        addGames(player1, 5);
        addGames(player2, 6);
        addGames(player1, 1);

        Assertions.assertEquals(6, matchScoreModel.getGames().get(player1));
        Assertions.assertEquals(6, matchScoreModel.getGames().get(player2));

        for (int i = 0; i < 5; i++) {
            matchScoreCalculationService.pointWon(matchScoreModel, player1);
        }
        for (int i = 0; i < 5; i++) {
            matchScoreCalculationService.pointWon(matchScoreModel, player2);
        }
        for (int i = 0; i < 2; i++) {
            matchScoreCalculationService.pointWon(matchScoreModel, player1);
        }

        Assertions.assertEquals(1, matchScoreModel.getSets().get(player1));
    }

    @Test
    void testMatchWon() {

        addGames(player1, 6);
        addGames(player2, 0);

        Assertions.assertEquals(1, matchScoreModel.getSets().get(player1));

        addGames(player1, 6);
        addGames(player2, 0);

        Assertions.assertTrue(matchScoreCalculationService.isMatchFinished(matchScoreModel));
        Assertions.assertEquals(2, matchScoreModel.getSets().get(player1));

        try {
            matchScoreCalculationService.pointWon(matchScoreModel, player1);
        } catch (BusinessException ignored) {}

        Assertions.assertEquals(2, matchScoreModel.getSets().get(player1));
    }

    @Test
    void testSetWon7_5() {

        addGames(player1, 5);
        addGames(player2, 5);
        addGames(player1, 2);

        Assertions.assertEquals(1, matchScoreModel.getSets().get(player1));
    }

    @Test
    void testPointWon40_40() {

        matchScoreCalculationService.pointWon(matchScoreModel, player1);
        matchScoreCalculationService.pointWon(matchScoreModel, player1);
        matchScoreCalculationService.pointWon(matchScoreModel, player1);

        matchScoreCalculationService.pointWon(matchScoreModel, player2);
        matchScoreCalculationService.pointWon(matchScoreModel, player2);
        matchScoreCalculationService.pointWon(matchScoreModel, player2);

        matchScoreCalculationService.pointWon(matchScoreModel, player1);

        Assertions.assertEquals(0, matchScoreModel.getGames().get(player1));
    }

    @Test
    void testPointWon40_0() {

        matchScoreCalculationService.pointWon(matchScoreModel, player1);
        matchScoreCalculationService.pointWon(matchScoreModel, player1);
        matchScoreCalculationService.pointWon(matchScoreModel, player1);
        matchScoreCalculationService.pointWon(matchScoreModel, player1);

        Assertions.assertEquals(1, matchScoreModel.getGames().get(player1));
    }
}
