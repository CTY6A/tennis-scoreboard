package com.stubedavd.match.model.service;

import com.stubedavd.player.model.domain.PlayerDomain;
import com.stubedavd.exception.BusinessException;
import com.stubedavd.match.model.domain.MatchGame;
import com.stubedavd.match.model.domain.RegularGameScore;
import com.stubedavd.match.model.domain.MatchScoreModel;

public class MatchScoreCalculationService {

    // TODO: Класс содержит в себе всю значимую бизнес-логику по подсчёту очков, геймов и сетов.
        // Объекты, которыми он оперирует, являются "анемичными" моделями —
        // простыми контейнерами данных практически без собственного поведения. Сервис напрямую читает и записывает их поля.
        // Это главная архитектурная проблема этой части логики. По этим причинам:
        //
        //  - Нарушение инкапсуляции: Данные (в моделях) и поведение (в `MatchScoreCalculationService`) разделены.
            //  Любой другой сервис может так же напрямую изменить счёт матча, и объект `MatchScoreModel` не сможет себя защитить.
        //  - Процедурный стиль: Вместо объектно-ориентированного подхода, где объекты сами управляют своим состоянием
            //  (и начисление очков происходит в духе `matchScoreModel.getPoints().addScore(player)`), получается процедурный код,
            //  который манипулирует внешними структурами данных.
        //  - Жёсткая связанность (Tight Coupling) и низкая связность (Low Cohesion):
            //  Сервис тесно связан с внутренним устройством `MatchScoreModel`. При этом логика,
            //  относящаяся к одному понятию (счёт), размазана по разным классам (модели и сервису).
        //  - Сложность тестирования: Чтобы протестировать один конкретный сценарий (например, переход от "ровно" к "преимуществу"),
            //  нужно разбираться во множестве `if` и переходов по методам. Это сложно и хрупко.
        //
        // Как исправить: Провести рефакторинг классов моделей с переходом к "богатой" доменной модели.

    // TODO: Сервис слишком много знает о внутреннем устройстве класса MatchScoreModel, а также полностью управляет внутренним состоянием моделей,
        // что повышает связанность кода и делает его сложнее в поддержке и рефакторинге.
        // Решение — реализация методов для работы с собственными данными в классах доменной модели.

    // В коде много лишних пустых строк, которые не улучшают читаемость (например, в методах с одной строкой).

    // TODO: Класс, отвечающий за обработку счёта в текущем матче, напрямую работает с JPA Entity, что смешивает слои.
        // (см. файл "Принцип разделения ответственности (Separation of Concerns).md" в этом же пакете)
        // Это должно исправиться автоматически после рефакторинга доменных моделей.

    public boolean isMatchFinished(MatchScoreModel matchScoreModel) {

        return matchScoreModel.isMatchFinished();
    }

    public void pointWon(MatchScoreModel matchScoreModel, PlayerDomain player) {

        if (isMatchFinished(matchScoreModel)) {

            throw new BusinessException("Match already finished");
        }

        if (isTieBreak(matchScoreModel)) {

            tieBreakProcessing(matchScoreModel, player);
        } else {

            matchScoreModel.getPoints().addScore(player);

            if (isGameWon(matchScoreModel, player)) {

                // Здесь уже два уровня вложенности (if внутри if) и вызывается метод, в котором в свою очередь тоже
                    // логика с вложенностью и вызовом методов внутри if-else — такой код сложно читать, тестировать и поддерживать.
                gameWonProcessing(matchScoreModel, player);
            }
        }
    }

    private void gameWonProcessing(MatchScoreModel matchScoreModel, PlayerDomain player) {

        matchScoreModel.setPoints(new RegularGameScore(matchScoreModel.getPlayer1(), matchScoreModel.getPlayer2()));

        matchScoreModel.getGames().addScore(player);

        if (isSetWon(matchScoreModel, player)) {

            setWonProcessing(matchScoreModel, player);
        }
    }

    private void setWonProcessing(MatchScoreModel matchScoreModel, PlayerDomain player) {

        recordScore(matchScoreModel);

        matchScoreModel.setGames(new MatchGame(matchScoreModel.getPlayer1(), matchScoreModel.getPlayer2()));

        matchScoreModel.getSets().addScore(player);

        if (isMatchWon(matchScoreModel, player)) {

            matchScoreModel.setMatchFinished(true);
            matchScoreModel.setWinner(player);
        }
    }

    private void recordScore(MatchScoreModel matchScoreModel) {

        matchScoreModel.getScore().add(matchScoreModel.getGames());
    }

    private void tieBreakProcessing(MatchScoreModel matchScoreModel, PlayerDomain player) {

        matchScoreModel.getPoints().addScore(player);

        if (isTieBreakWon(matchScoreModel, player)) {

            matchScoreModel.setPoints(new RegularGameScore(matchScoreModel.getPlayer1(), matchScoreModel.getPlayer2()));

            matchScoreModel.getGames().addScore(player);

            setWonProcessing(matchScoreModel, player);
        }
    }

    private boolean isTieBreak(MatchScoreModel matchScoreModel) {
        return matchScoreModel.getGames().isTieBreak();
    }

    private boolean isGameWon(MatchScoreModel matchScoreModel, PlayerDomain player) {

        return matchScoreModel.getPoints().isRoundWon(player);
    }

    private boolean isSetWon(MatchScoreModel matchScoreModel, PlayerDomain player) {

        return matchScoreModel.getGames().isRoundWon(player);
    }

    private boolean isMatchWon(MatchScoreModel matchScoreModel, PlayerDomain player) {

        return matchScoreModel.getSets().isRoundWon(player);
    }

    private boolean isTieBreakWon(MatchScoreModel matchScoreModel, PlayerDomain player) {

        return matchScoreModel.getPoints().isRoundWon(player);
    }

    private boolean checkTieBreak(MatchScoreModel matchScoreModel) {

        return matchScoreModel.getGames().isTieBreak();
    }
}
