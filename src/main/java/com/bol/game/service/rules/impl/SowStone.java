package com.bol.game.service.rules.impl;

import com.bol.game.domain.Board;
import com.bol.game.domain.Pit;
import com.bol.game.domain.TurnInfo;
import com.bol.game.exception.MainPitSelectedException;
import com.bol.game.exception.StoneCountZeroException;
import com.bol.game.service.rules.GameRule;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Order(1)
public class SowStone extends GameRule {

    @Override
    public void applyRule(Board board) {
        Map<Integer, Pit> pitMap = board.getPitMap();
        Integer startIndex = board.getTurnInfo().getSelectedPitIndex();
        checkMainPitSelected(startIndex, pitMap);
        Integer sowStoneCount = pitMap.get(startIndex).getStones();
        checkStoneCount(sowStoneCount);
        Integer index = startIndex;
        while (sowStoneCount > 0) {
            index += 1;
            if (index >= pitMap.size()) {
                index = 1;
            }
            if (isSowable(board.getTurnInfo(), pitMap.get(index))) {
                pitMap.get(startIndex).removeOneStone();
                pitMap.get(index).addOneStone();
                sowStoneCount -= 1;
            }
        }
        board.getTurnInfo().setLastUpdatedPitIndex(index);
    }

    private boolean isSowable(TurnInfo turnInfo, Pit pit) {
        return !(pit.isMainPit() && pit.isPlayerDown() != turnInfo.isPlayerDownTurn());
    }

    private void checkMainPitSelected(Integer startIndex, Map<Integer, Pit> pitMap) {
        if (pitMap.get(startIndex).isMainPit()) {
            throw new MainPitSelectedException();
        }
    }

    private void checkStoneCount(Integer sowStoneCount) {
        if (sowStoneCount.equals(0)) {
            throw new StoneCountZeroException();
        }
    }

}
