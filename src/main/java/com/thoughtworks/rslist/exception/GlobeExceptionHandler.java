package com.thoughtworks.rslist.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobeExceptionHandler {
    @ExceptionHandler({InvalidRequestParamException.class,InvalidIndexException.class})
    public ResponseEntity handleException(Exception ex){

        if(ex instanceof InvalidRequestParamException){
            CommentError commentError =new CommentError("invalid request param");
            return ResponseEntity.badRequest().body(commentError);
        }
        if(ex instanceof  InvalidIndexException){
            CommentError commentError =new CommentError("invalid index");
            return ResponseEntity.badRequest().body(commentError);
        }

        CommentError commentError =new CommentError("something wrong");
        return ResponseEntity.badRequest().body(commentError);

    }

}
