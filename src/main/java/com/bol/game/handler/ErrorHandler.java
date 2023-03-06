package com.bol.game.handler;

import com.bol.game.dto.ErrorDetail;
import com.bol.game.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({MainPitSelectedException.class,
            NoBoardFoundException.class,
            FinishedGamePlayedException.class,
            PitCountNotSuitForGameException.class,
            WrongPitSelectedException.class,
            StoneCountZeroException.class,
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorDetail> handleNoDataFound(RuntimeException ex) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(Instant.now().getEpochSecond());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle("Validation Failed");
        errorDetail.setDetail(ex.getMessage());
        errorDetail.setDeveloperMessage(ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }


}
