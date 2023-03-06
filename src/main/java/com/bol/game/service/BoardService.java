package com.bol.game.service;

import com.bol.game.config.KalahaProperty;
import com.bol.game.domain.Board;
import com.bol.game.domain.Pit;
import com.bol.game.domain.TurnInfo;
import com.bol.game.exception.NoBoardFoundException;
import com.bol.game.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final KalahaProperty kalahaProperty;

    public Board createBoard() {
        Board board = Board.builder().pitMap(createPitMap())
                .turnInfo(TurnInfo.builder().playerDownTurn(true).build())
                .id(UUID.randomUUID())
                .finished(false)
                .build();
        boardRepository.save(board);
        return board;
    }

    private Map<Integer, Pit> createPitMap() {
        Map<Integer, Pit> pitMap = new HashMap<>();
        for (int i = 1; i <= kalahaProperty.getPitCountPerUser() * 2; i++) {
            boolean playerDown = i <= kalahaProperty.getPitCountPerUser();
            if (i % kalahaProperty.getPitCountPerUser() == 0) {
                pitMap.put(i, Pit.builder().mainPit(true).stones(0).playerDown(playerDown).build());
            } else {
                pitMap.put(i, Pit.builder().mainPit(false).stones(kalahaProperty.getStoneCount()).playerDown(playerDown).build());
            }
        }
        return pitMap;
    }

    public Board getBoard(UUID boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isPresent()) {
            return board.get();
        }
        throw new NoBoardFoundException();
    }
}
