package com.bol.game.controller;

import com.bol.game.domain.Board;
import com.bol.game.domain.Pit;
import com.bol.game.dto.PitDto;
import com.bol.game.dto.ResponseCreate;
import com.bol.game.dto.ResponseGame;
import com.bol.game.exception.FinishedGamePlayedException;
import com.bol.game.exception.PitCountNotSuitForGameException;
import com.bol.game.exception.WrongPitSelectedException;
import com.bol.game.service.BoardService;
import com.bol.game.service.GamePlayService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final BoardService boardService;
    private final GamePlayService gamePlayService;

    @PostMapping(value = "/games")
    public ResponseEntity<ResponseCreate> create() {
        Board board = boardService.createBoard();
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseCreate response = ResponseCreate.builder().id(board.getId()).build();
        addGetLink(responseHeaders, response, board.getId());
        return new ResponseEntity<>(response, responseHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/games/{id}")
    public ResponseEntity<ResponseGame> retrieve(@PathVariable UUID id) {
        Board board = boardService.getBoard(id);
        ResponseGame response = setResponseGame(board);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/games/{gameId}/{pitId}")
    public ResponseEntity<ResponseGame> play(@PathVariable UUID gameId,
                                             @PathVariable Integer pitId) {
        Board board = boardService.getBoard(gameId);
        validatePlay(board, pitId);
        board.getTurnInfo().setSelectedPitIndex(pitId);
        board = gamePlayService.move(board);
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseGame response = setResponseGame(board);
        addGetLink(responseHeaders, response, board.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private void validatePlay(Board board, Integer pitId) {
        if (board.isFinished()) {
            throw new FinishedGamePlayedException(board.getId());
        }
        if (pitId.compareTo(board.getPitMap().size()) > 0 || pitId <= 0) {
            throw new PitCountNotSuitForGameException(pitId, board.getPitMap().size());
        }
        if (board.getTurnInfo().isPlayerDownTurn() != board.getPitMap().get(pitId).isPlayerDown()) {
            throw new WrongPitSelectedException(pitId);
        }
    }


    private void addGetLink(HttpHeaders responseHeaders, RepresentationModel<?> response, UUID gameId) {
        URI newPollUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(gameId).toUri();
        responseHeaders.setLocation(newPollUri);
        response.add(linkTo(methodOn(GameController.class).retrieve(gameId)).withSelfRel());
    }

    private ResponseGame setResponseGame(Board board) {
        return ResponseGame.builder().id(board.getId())
                .board(createResponseList(board.getPitMap()))
                .playerTurn(changePlayerToString(board.getTurnInfo().isPlayerDownTurn()))
                .build();
    }


    private List<PitDto> createResponseList(Map<Integer, Pit> pitMap) {
        return pitMap.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey))
                .map(e -> PitDto.builder()
                        .stones(e.getValue().getStones())
                        .index(e.getKey())
                        .playerBy(changePlayerToString(e.getValue().isPlayerDown()))
                        .build())
                .collect(Collectors.toList());
    }

    private String changePlayerToString(boolean playerBy) {
        return playerBy ? "1" : "0";
    }
}
