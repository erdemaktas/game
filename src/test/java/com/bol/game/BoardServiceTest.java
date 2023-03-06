package com.bol.game;

import com.bol.game.config.KalahaProperty;
import com.bol.game.domain.Board;
import com.bol.game.exception.NoBoardFoundException;
import com.bol.game.repository.BoardRepository;
import com.bol.game.service.BoardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BoardServiceTest {

    @Mock
    BoardRepository boardRepository;

    @InjectMocks
    BoardService boardService;

    @Mock
    KalahaProperty kalahaProperty;

    @Test
    void test_create_board_service_success() {
        when(kalahaProperty.getPitCountPerUser()).thenReturn(7);
        when(kalahaProperty.getStoneCount()).thenReturn(6);

        final Board board = mock(Board.class);
        when(boardRepository.save(any())).then(returnsFirstArg());
        Board boardResponse = boardService.createBoard();
        verify(boardRepository).save(any());
        assertFalse(boardResponse.isFinished());
        assertEquals(boardResponse.getPitMap().size(), kalahaProperty.getPitCountPerUser() * 2);
        assertTrue(boardResponse.getTurnInfo().isPlayerDownTurn());
        assertFalse(boardResponse.getMainPits().stream().anyMatch(main -> main.getStones().compareTo(0) != 0));
        assertEquals(72, boardResponse.getPitMap().values().stream().mapToInt(x -> x.getStones()).sum());

    }

    @Test
    void test_get_board_service_success() {
        UUID uuid = UUID.randomUUID();
        Board board = Board.builder().finished(false).id(uuid).build();
        when(boardRepository.findById(any())).thenReturn(Optional.of(board));
        Board boardResponse = boardService.getBoard(uuid);
        assertFalse(boardResponse.isFinished());
        assertEquals(boardResponse.getId(), uuid);

    }

    @Test
    void test_get_board_not_found_exception_failed() {
        UUID uuid = UUID.randomUUID();
        when(boardRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NoBoardFoundException.class, () -> {
            boardService.getBoard(uuid);
        });

    }
}
