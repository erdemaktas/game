package com.bol.game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoBoardFoundException extends RuntimeException {
    public NoBoardFoundException() {
        super("Board can not found");
    }
}
