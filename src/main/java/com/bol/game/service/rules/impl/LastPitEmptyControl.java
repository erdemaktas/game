package com.bol.game.service.rules.impl;

import com.bol.game.domain.Board;
import com.bol.game.domain.Pit;
import com.bol.game.domain.TurnInfo;
import com.bol.game.service.rules.GameRule;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Order(2)
public class LastPitEmptyControl extends GameRule {

    @Override
    public void applyRule(Board board) {
        Map<Integer, Pit> pitMap = board.getPitMap();
        if (lastPitWasEmpty(board.getTurnInfo(), pitMap)) {
            Integer ownMainPitIndex = board.getMainPitIndexByPlayerTurn();
            if (!ownMainPitIndex.equals(board.getTurnInfo().getLastUpdatedPitIndex())) {
                pitMap.get(ownMainPitIndex).addOneStone();
                pitMap.get(board.getTurnInfo().getLastUpdatedPitIndex()).removeAllStone();

                collectOppositePitStones(board, ownMainPitIndex);
            }
        }
    }

    private boolean lastPitWasEmpty(TurnInfo turnInfo, Map<Integer, Pit> pitMap) {
        Pit lastPit = pitMap.get(turnInfo.getLastUpdatedPitIndex());
        return lastPit.isPlayerDown() == turnInfo.isPlayerDownTurn() && lastPit.getStones().equals(1);
    }

    private void collectOppositePitStones(Board board, Integer ownMainPitIndex) {
        Integer oppositeIndex = findOppositeIndex(board.getPitMap(), board.getTurnInfo().getLastUpdatedPitIndex());
        Pit oppPit = board.getPitMap().get(oppositeIndex);
        if (oppPit.getStones() > 0) {
            board.getPitMap().get(ownMainPitIndex).addStones(oppPit.getStones());
            oppPit.removeAllStone();
        }
    }

    public Integer findOppositeIndex(Map<Integer, Pit> pitMap, Integer lastUpdatedPitIndex) {
        return pitMap.size() - lastUpdatedPitIndex;
    }

}
