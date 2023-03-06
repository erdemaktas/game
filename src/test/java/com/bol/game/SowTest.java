package com.bol.game;

import com.bol.game.config.KalahaProperty;
import com.bol.game.domain.Board;
import com.bol.game.domain.Pit;
import com.bol.game.exception.MainPitSelectedException;
import com.bol.game.exception.StoneCountZeroException;
import com.bol.game.repository.BoardRepository;
import com.bol.game.service.BoardService;
import com.bol.game.service.rules.impl.SowStone;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SowTest {

    @InjectMocks
    SowStone sowStone;

    @InjectMocks
    BoardService boardService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    KalahaProperty kalahaProperty;

    private Board createBoard() {
        return boardService.createBoard();
    }


    @Test
    void test_remove_selected_pit_stone_count() {
        when(kalahaProperty.getPitCountPerUser()).thenReturn(7);
        when(kalahaProperty.getStoneCount()).thenReturn(6);

        Board board = createBoard();
        board.getTurnInfo().setSelectedPitIndex(1);
        sowStone.applyRule(board);
        assertEquals(board.getPitMap().get(1).getStones(), Integer.valueOf(0));
    }

    @Test
    void test_increment_one_next_pit_stone_count() {
        when(kalahaProperty.getPitCountPerUser()).thenReturn(7);
        when(kalahaProperty.getStoneCount()).thenReturn(6);

        Board board = createBoard();
        board.getTurnInfo().setSelectedPitIndex(1);
        sowStone.applyRule(board);
        assertEquals(board.getPitMap().get(2).getStones(), Integer.valueOf(7));
        assertEquals(board.getPitMap().get(3).getStones(), Integer.valueOf(7));
        assertEquals(board.getPitMap().get(4).getStones(), Integer.valueOf(7));
        assertEquals(board.getPitMap().get(5).getStones(), Integer.valueOf(7));
        assertEquals(board.getPitMap().get(6).getStones(), Integer.valueOf(7));
        assertEquals(board.getPitMap().get(7).getStones(), Integer.valueOf(1));
    }

    @Test
    void test_last_pit_index_updated() {
        when(kalahaProperty.getPitCountPerUser()).thenReturn(7);
        when(kalahaProperty.getStoneCount()).thenReturn(6);

        Board board = createBoard();
        board.getTurnInfo().setSelectedPitIndex(1);
        sowStone.applyRule(board);
        assertEquals(board.getPitMap().get(board.getMainPitIndexByPlayerTurn()).getStones(), Integer.valueOf(1));
        assertEquals(board.getTurnInfo().getLastUpdatedPitIndex(), Integer.valueOf(7));
    }

    @Test
    void test_not_add_stone_other_main_pit() {
        when(kalahaProperty.getPitCountPerUser()).thenReturn(7);
        when(kalahaProperty.getStoneCount()).thenReturn(35);

        Board board = createBoard();
        board.getTurnInfo().setSelectedPitIndex(6);
        sowStone.applyRule(board);
        Pit otherMainPit = board.getMainPits().stream().filter(p -> p.isPlayerDown() != board.getTurnInfo().isPlayerDownTurn()).findFirst().get();
        Pit ownMainPit = board.getMainPits().stream().filter(p -> p.isPlayerDown() == board.getTurnInfo().isPlayerDownTurn()).findFirst().get();
        assertEquals(otherMainPit.getStones(), Integer.valueOf(0));
        assertEquals(ownMainPit.getStones(), Integer.valueOf(3));

    }

    @Test
    void test_sow_two_lapfor_a_pit() {
        when(kalahaProperty.getPitCountPerUser()).thenReturn(7);
        when(kalahaProperty.getStoneCount()).thenReturn(6);

        Board board = createBoard();
        board.getTurnInfo().setSelectedPitIndex(6);
        sowStone.applyRule(board);
        assertEquals(board.getPitMap().get(4).getStones(), Integer.valueOf(6));
    }

    @Test
    void test_not_sow_main_pit() {
        when(kalahaProperty.getPitCountPerUser()).thenReturn(7);
        when(kalahaProperty.getStoneCount()).thenReturn(6);

        Board board = createBoard();
        board.getTurnInfo().setSelectedPitIndex(7);
        assertThrows(MainPitSelectedException.class, () -> {
            sowStone.applyRule(board);
        });
    }

    @Test
    void test_stone_count_zero_selected() {
        when(kalahaProperty.getPitCountPerUser()).thenReturn(7);
        when(kalahaProperty.getStoneCount()).thenReturn(6);

        Board board = createBoard();
        board.getTurnInfo().setSelectedPitIndex(1);
        sowStone.applyRule(board);
        board.getTurnInfo().setSelectedPitIndex(1);
        assertThrows(StoneCountZeroException.class, () -> {
            sowStone.applyRule(board);
        });
    }

}
