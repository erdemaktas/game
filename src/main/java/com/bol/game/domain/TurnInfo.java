package com.bol.game.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class TurnInfo {
    @Setter
    private Integer selectedPitIndex;
    @Setter
    private Integer lastUpdatedPitIndex;

    private boolean playerDownTurn;


    public void changePlayerTurn() {
        this.playerDownTurn = !this.playerDownTurn;
    }
}
