package com.bol.game;

import com.bol.game.config.KalahaProperty;
import com.bol.game.domain.Board;
import com.bol.game.repository.BoardRepository;
import com.bol.game.service.BoardService;
import com.bol.game.service.rules.impl.CalculatePlayerTurn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CalculatePlayerTurnTest {

    @InjectMocks
    BoardService boardService;

    @InjectMocks
    CalculatePlayerTurn calculatePlayerTurn;

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
    void test_next_player_same_when_last_stone_at_main_pit() {
        Board board = createBoard();
        boolean playerDownTurn = board.getTurnInfo().isPlayerDownTurn();
        board.getTurnInfo().setLastUpdatedPitIndex(board.getMainPitIndexByPlayerTurn());
        calculatePlayerTurn.applyRule(board);
        assertEquals(board.getTurnInfo().isPlayerDownTurn(), playerDownTurn);
    }

    @Test
    void test_next_player_changed_when_last_stone_NOT_at_main_pit() {
        Board board = createBoard();
        boolean playerDownTurn = board.getTurnInfo().isPlayerDownTurn();
        board.getTurnInfo().setLastUpdatedPitIndex(6);
        calculatePlayerTurn.applyRule(board);
        assertEquals(board.getTurnInfo().isPlayerDownTurn(), !playerDownTurn);
    }
}
