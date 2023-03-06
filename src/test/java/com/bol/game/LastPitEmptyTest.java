package com.bol.game;

import com.bol.game.config.KalahaProperty;
import com.bol.game.domain.Board;
import com.bol.game.repository.BoardRepository;
import com.bol.game.service.BoardService;
import com.bol.game.service.rules.impl.LastPitEmptyControl;
import com.bol.game.service.rules.impl.SowStone;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LastPitEmptyTest {

    @InjectMocks
    BoardService boardService;

    @InjectMocks
    LastPitEmptyControl lastPitEmptyControl;

    @InjectMocks
    SowStone sowStone;

    @Mock
    BoardRepository boardRepository;

    @Mock
    KalahaProperty kalahaProperty;

    private Board createBoard() {
        return boardService.createBoard();
    }

    @Test
    void test_no_change_on_last_pit_when_last_pit_not_empty() {
        when(kalahaProperty.getPitCountPerUser()).thenReturn(7);
        when(kalahaProperty.getStoneCount()).thenReturn(6);

        Board board = createBoard();
        board.getTurnInfo().setSelectedPitIndex(4);
        sowStone.applyRule(board);
        board.getTurnInfo().setSelectedPitIndex(2);
        sowStone.applyRule(board);
        lastPitEmptyControl.applyRule(board);
        assertNotEquals(board.getPitMap().get(board.getTurnInfo().getLastUpdatedPitIndex()).getStones(), Integer.valueOf(0));
    }

    @Test
    void test_no_change_on_last_pit_when_last_pit_is_main_pit() {
        when(kalahaProperty.getPitCountPerUser()).thenReturn(7);
        when(kalahaProperty.getStoneCount()).thenReturn(6);

        Board board = createBoard();
        board.getTurnInfo().setSelectedPitIndex(1);
        sowStone.applyRule(board);
        lastPitEmptyControl.applyRule(board);
        assertNotEquals(board.getPitMap().get(board.getTurnInfo().getLastUpdatedPitIndex()).getStones(), Integer.valueOf(0));
        assertEquals(board.getPitMap().get(board.getTurnInfo().getLastUpdatedPitIndex()).getStones(), Integer.valueOf(1));
    }

    @Test
    void test_change_on_last_pit_when_last_pit_empty() {
        when(kalahaProperty.getPitCountPerUser()).thenReturn(7);
        when(kalahaProperty.getStoneCount()).thenReturn(8);

        Board board = createBoard();
        board.getPitMap().get(1).removeAllStone();
        board.getTurnInfo().setSelectedPitIndex(6);
        sowStone.applyRule(board);
        lastPitEmptyControl.applyRule(board);
        assertEquals(board.getPitMap().get(board.getTurnInfo().getLastUpdatedPitIndex()).getStones(), Integer.valueOf(0));
        Integer oppositeIndex = lastPitEmptyControl.findOppositeIndex(board.getPitMap(), board.getTurnInfo().getLastUpdatedPitIndex());
        assertEquals(board.getPitMap().get(oppositeIndex).getStones(), Integer.valueOf(0));
        assertEquals(oppositeIndex, Integer.valueOf(13));
        assertEquals(board.getPitMap().get(board.getMainPitIndexByPlayerTurn()).getStones(), Integer.valueOf(11));
        assertEquals(board.getPitMap().get(14).getStones(), Integer.valueOf(0));
        assertEquals(board.getTurnInfo().getLastUpdatedPitIndex(), Integer.valueOf(1));
    }
}
