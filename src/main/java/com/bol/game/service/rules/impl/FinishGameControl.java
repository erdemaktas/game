package com.bol.game.service.rules.impl;

import com.bol.game.domain.Board;
import com.bol.game.domain.Pit;
import com.bol.game.service.rules.GameRule;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Component
@Order(4)
public class FinishGameControl extends GameRule {

    @Override
    public void applyRule(Board board) {
        if (isFinishGame(board)) {
            moveStonesToMainPit(board);
            board.finishGame();
        }
    }

    private void moveStonesToMainPit(Board board) {
        Map<Boolean, Integer> totalCountMap = new HashMap<>();
        totalCountMap.put(true, 0);
        totalCountMap.put(false, 0);
        for (Pit pit : board.getPitMap().values()) {
            if (!pit.isMainPit()) {
                totalCountMap.put(pit.isPlayerDown(), pit.getStones() + totalCountMap.get(pit.isPlayerDown()));
                pit.removeAllStone();
            }
        }
        board.getMainPits()
                .forEach(mainPit -> mainPit.addStones(totalCountMap.get(mainPit.isPlayerDown())));
    }

    private boolean isFinishGame(Board board) {
        Map<Boolean, Integer> stoneCountMap = checkStoneCountByPlayer(board.getPitMap());
        return stoneCountMap.containsValue(0);
    }

    private Map<Boolean, Integer> checkStoneCountByPlayer(Map<Integer, Pit> pitMap) {
        return pitMap.values().stream()
                .filter(pit -> !pit.isMainPit())
                .collect(groupingBy(Pit::isPlayerDown, summingInt(Pit::getStones)));
    }
}
