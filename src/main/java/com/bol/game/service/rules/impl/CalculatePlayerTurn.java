package com.bol.game.service.rules.impl;

import com.bol.game.domain.Board;
import com.bol.game.service.rules.GameRule;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class CalculatePlayerTurn extends GameRule {
    @Override
    public void applyRule(Board board) {
        if (!board.getPitMap().get(board.getTurnInfo().getLastUpdatedPitIndex()).isMainPit()) {
            board.getTurnInfo().changePlayerTurn();
        }
    }

}
