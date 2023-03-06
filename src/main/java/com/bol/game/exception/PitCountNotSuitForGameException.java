package com.bol.game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PitCountNotSuitForGameException extends RuntimeException {
    public PitCountNotSuitForGameException(Integer selectedPit, Integer maxPitCount) {
        super("Selected pit (" + selectedPit + ") must be between 1 and " + maxPitCount);
    }
}
