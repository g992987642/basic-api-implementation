package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.UserList;
import org.apache.catalina.User;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
