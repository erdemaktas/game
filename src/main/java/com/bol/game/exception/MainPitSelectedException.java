package com.bol.game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MainPitSelectedException extends RuntimeException {
    public MainPitSelectedException() {
        super("Main pit can not select for sowing");
    }
}
