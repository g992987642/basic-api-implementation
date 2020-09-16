package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.UserList;
import com.thoughtworks.rslist.exception.CommentError;
import org.apache.catalina.User;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

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
            return ResponseEntity.badRequest().body(commentError);
    }

}
