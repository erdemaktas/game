package com.bol.game.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@Document
@Builder
@Getter
public class Board {

    @Id
    private UUID id;
    private Map<Integer, Pit> pitMap;
    private TurnInfo turnInfo;
    private boolean finished;

    public Integer getMainPitIndexByPlayerTurn() {
        return this.pitMap.entrySet().stream().filter(entry -> entry.getValue().isMainPit() && entry.getValue().isPlayerDown() == turnInfo.isPlayerDownTurn())
                .findFirst().orElseThrow().getKey();
    }

    public List<Pit> getMainPits() {
        return pitMap.values().stream().filter(Pit::isMainPit)
                .toList();
    }

    public void finishGame() {
        this.finished = true;
    }

}

