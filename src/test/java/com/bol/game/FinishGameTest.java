package com.bol.game;

import com.bol.game.config.KalahaProperty;
import com.bol.game.domain.Board;
import com.bol.game.repository.BoardRepository;
import com.bol.game.service.BoardService;
import com.bol.game.service.rules.impl.FinishGameControl;
import com.bol.game.service.rules.impl.LastPitEmptyControl;
import com.bol.game.service.rules.impl.SowStone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FinishGameTest {

    @InjectMocks
    BoardService boardService;

    @InjectMocks
    FinishGameControl finishGameControl;

    @InjectMocks
    SowStone sowStone;

    @InjectMocks
    LastPitEmptyControl lastPitEmptyControl;

    @Mock
    BoardRepository boardRepository;

    @Mock
    KalahaProperty kalahaProperty;

    @BeforeEach
    void initializeConfig() {
        when(kalahaProperty.getPitCountPerUser()).thenReturn(7);
        when(kalahaProperty.getStoneCount()).thenReturn(6);
    }

    private Board createBoard() {
        return boardService.createBoard();
    }


    @Test
    void test_game_not_finished() {
        Board board = createBoard();
        board.getTurnInfo().setSelectedPitIndex(1);
        sowStone.applyRule(board);
        finishGameControl.applyRule(board);
        assertFalse(board.isFinished());
    }

    @Test
    void test_game_finished() {
        Board board = createBoard();
        board.getPitMap().get(board.getMainPitIndexByPlayerTurn()).addOneStone();
        board.getPitMap().values().stream().filter(pit -> pit.isPlayerDown() && !pit.isMainPit()).forEach(pit -> pit.removeAllStone());
        finishGameControl.applyRule(board);
        assertTrue(board.isFinished());
    }
}
