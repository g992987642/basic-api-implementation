package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


public interface UserService {
    public void register(UserDto userDto);

    public List<UserDto> getUsers();

    public UserDto getUsersById(int id);

    public void deleteUsersById(int id);
}
