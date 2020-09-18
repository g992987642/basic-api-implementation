package com.thoughtworks.rslist.dto;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    public static List<UserDto> userList=new ArrayList<>();
    static {
        userList.add(new UserDto("guhao",18,"男","12345678@qq.com","12345678910"));
    }
}
