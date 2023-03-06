package com.bol.game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongPitSelectedException extends RuntimeException {
    public WrongPitSelectedException(Integer pitId) {
        super("Selected pit is not suitable for the selected user. pit id:" + pitId);
    }
}
