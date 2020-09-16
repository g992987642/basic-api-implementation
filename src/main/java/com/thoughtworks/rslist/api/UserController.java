package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.UserList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @PostMapping("/user/register")
    public void register(@RequestBody @Valid UserDto userDto){
        boolean isRegisterd=false;
        for(UserDto registerUser : UserList.userList){
            if(registerUser.getUserName().equals(userDto.getUserName())){
                isRegisterd=true;
            }
        }
        if(!isRegisterd){
            UserList.userList.add(userDto);
        }
    }
}
