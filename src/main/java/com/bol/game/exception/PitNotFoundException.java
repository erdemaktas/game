package com.bol.game.exception;

public class PitNotFoundException extends RuntimeException {
    public PitNotFoundException(Integer pitId) {
        super("Pit not found. Pit Id:" + pitId);
    }

}
