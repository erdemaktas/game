package com.bol.game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StoneCountZeroException extends RuntimeException {
    public StoneCountZeroException() {
        super("There has to be stone in the selected pit.");
    }
}
