package com.stubedavd.service.impl;

import com.stubedavd.dto.PlayerDto;
import com.stubedavd.exception.BusinessException;
import com.stubedavd.model.MatchScoreModel;
import com.stubedavd.service.MatchScoreCalculationService;

import java.util.Map;

public class MatchScoreCalculationServiceImpl implements MatchScoreCalculationService {

    @Override
    public void pointWon(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        if (isMatchFinished(matchScoreModel)) {
            throw new BusinessException("Match already finished");
        }

        if (!isTieBreak(matchScoreModel)) {

            addPoint(matchScoreModel, playerDto);

            if (isGameWon(matchScoreModel, playerDto)) {

                resetPoints(matchScoreModel);

                addGame(matchScoreModel, playerDto);

                if (isSetWon(matchScoreModel, playerDto)) {

                    resetGames(matchScoreModel);

                    addSet(matchScoreModel, playerDto);

                    if (isMatchWon(matchScoreModel, playerDto)) {

                        matchScoreModel.setMatchFinished(true);
                        matchScoreModel.setWinner(playerDto);
                    }
                } else if (checkTieBreak(matchScoreModel)) {

                    matchScoreModel.setTieBreak(true);
                }
            }
        } else {

            addPoint(matchScoreModel, playerDto);

            if (isTieBreakWon(matchScoreModel, playerDto)) {

                resetPoints(matchScoreModel);
                resetGames(matchScoreModel);
                matchScoreModel.setTieBreak(false);

                addSet(matchScoreModel, playerDto);

                if (isMatchWon(matchScoreModel, playerDto)) {

                    matchScoreModel.setMatchFinished(true);
                    matchScoreModel.setWinner(playerDto);
                }
            }
        }
    }

    private Boolean isTieBreakWon(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        Integer tieBreakWinnerPoints = 0;
        Integer tieBreakLoserPoints = 0;

        for(Map.Entry<PlayerDto, Integer> entry : matchScoreModel.getPoints().entrySet()) {

            if (playerDto.equals(entry.getKey())) {
                tieBreakWinnerPoints = entry.getValue();
            } else {
                tieBreakLoserPoints = entry.getValue();
            }
        }

        return tieBreakWinnerPoints >= 7 && tieBreakWinnerPoints > tieBreakLoserPoints + 1;
    }

    private Boolean checkTieBreak(MatchScoreModel matchScoreModel) {

        for (Map.Entry<PlayerDto, Integer> entry : matchScoreModel.getGames().entrySet()) {

            if (entry.getValue() != 6) {
                return false;
            }
        }

        return true;
    }

    private Boolean isMatchWon(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        return matchScoreModel.getSets().get(playerDto) == 2;
    }

    private void addSet(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        Map<PlayerDto, Integer> sets = matchScoreModel.getSets();
        sets.put(playerDto, sets.get(playerDto) + 1);
    }

    private void resetGames(MatchScoreModel matchScoreModel) {
        
        matchScoreModel.getGames().replaceAll((key, value) -> 0);
    }

    private void resetPoints(MatchScoreModel matchScoreModel) {

        matchScoreModel.getPoints().replaceAll((key, value) -> 0);
    }

    private Boolean isSetWon(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        Integer setWinnerGames = 0;
        Integer setLoserGames = 0;

        for(Map.Entry<PlayerDto, Integer> entry : matchScoreModel.getGames().entrySet()) {

            if (playerDto.equals(entry.getKey())) {
                setWinnerGames = entry.getValue();
            } else {
                setLoserGames = entry.getValue();
            }
        }

        return setWinnerGames >= 6 && setWinnerGames > setLoserGames + 1;
    }

    private void addGame(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        Map<PlayerDto, Integer> games = matchScoreModel.getGames();
        games.put(playerDto, games.get(playerDto) + 1);
    }

    private Boolean isGameWon(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        Integer gameWinnerPoints = 0;
        Integer gameLoserPoints = 0;

        for(Map.Entry<PlayerDto, Integer> entry : matchScoreModel.getPoints().entrySet()) {

            if (playerDto.equals(entry.getKey())) {
                gameWinnerPoints = entry.getValue();
            } else {
                gameLoserPoints = entry.getValue();
            }
        }

        return gameWinnerPoints >= 4 && gameWinnerPoints > gameLoserPoints + 1;
    }

    private void addPoint(MatchScoreModel matchScoreModel, PlayerDto playerDto) {

        Map<PlayerDto, Integer> points = matchScoreModel.getPoints();
        points.put(playerDto, points.get(playerDto) + 1);
    }

    private Boolean isTieBreak(MatchScoreModel matchScoreModel) {
        return matchScoreModel.getTieBreak();
    }

    @Override
    public Boolean isMatchFinished(MatchScoreModel matchScoreModel) {
        return matchScoreModel.getMatchFinished();
    }
}
