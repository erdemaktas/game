package com.bol.game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FinishedGamePlayedException extends RuntimeException {
    public FinishedGamePlayedException(UUID id) {
        super("You cannot play finished game. Id: " + id);
    }
}
