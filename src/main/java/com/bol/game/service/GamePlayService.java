package com.bol.game.service;

import com.bol.game.domain.Board;
import com.bol.game.repository.BoardRepository;
import com.bol.game.service.rules.GameRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GamePlayService {

    private final List<GameRule> gameRules;
    private final BoardRepository repository;

    public Board move(Board board) {
        gameRules.stream().forEach(r -> r.applyRule(board));
        repository.save(board);
        return board;
    }

}
