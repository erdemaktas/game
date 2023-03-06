package com.bol.game.domain;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Pit {
    private Integer stones;
    private boolean playerDown;
    private boolean mainPit;

    public void removeAllStone() {
        stones = 0;
    }

    public void addOneStone() {
        stones += 1;
    }

    public void removeOneStone() {
        stones -= 1;
    }

    public void addStones(Integer count) {
        stones += count;
    }

}
