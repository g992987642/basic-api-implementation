package com.thoughtworks.rslist.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobeExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobeExceptionHandler.class);

    @ExceptionHandler({InvalidRequestParamException.class,InvalidIndexException.class})
    public ResponseEntity handleException(Exception ex){

        if(ex instanceof InvalidRequestParamException){
            CommentError commentError =new CommentError("invalid request param");
            logger.error("[LOGGING]:"+commentError.getError());
            return ResponseEntity.badRequest().body(commentError);
        }
        if(ex instanceof  InvalidIndexException){
            CommentError commentError =new CommentError("invalid index");
            logger.error("[LOGGING]:"+commentError.getError());
            return ResponseEntity.badRequest().body(commentError);
        }

        CommentError commentError =new CommentError("something wrong");
        logger.error("[LOGGING]:"+commentError.getError());
        return ResponseEntity.badRequest().body(commentError);

    }

}
