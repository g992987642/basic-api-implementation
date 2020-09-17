package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.UserList;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.exception.GlobeExceptionHandler;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/user/register")
    public void register(@RequestBody @Valid UserDto userDto){
            UserList.userList.add(userDto);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers(){
        return ResponseEntity.ok().body(UserList.userList);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleException(MethodArgumentNotValidException ex){
            CommentError commentError =new CommentError("invalid user");
              logger.error("[LOGGING]:"+commentError.getError());
            return ResponseEntity.badRequest().body(commentError);
    }

}
