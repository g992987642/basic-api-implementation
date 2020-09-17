package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.UserList;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostMapping("/user/register")
    public ResponseEntity register(@RequestBody @Valid UserDto userDto){
        UserEntity userEntity = convertUserDtoToEntity(userDto);
        userRepository.save(userEntity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity getUsers(){
        return ResponseEntity.ok().body(
                UserList.userList
        );
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUsersById(@PathVariable int id){

        Optional<UserEntity> user = userRepository.findById(id);
        if(!user.isPresent()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(user);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleException(MethodArgumentNotValidException ex){
            CommentError commentError =new CommentError("invalid user");
              logger.error("[LOGGING]:"+commentError.getError());
            return ResponseEntity.badRequest().body(commentError);
    }

    public UserEntity convertUserDtoToEntity(UserDto userDto){

        UserEntity userEntity=UserEntity.builder()
                .userName(userDto.getUserName())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .gender(userDto.getGender())
                .phone(userDto.getPhone())
                .voteNum(userDto.getVoteNum())
                .build();
        return userEntity;
    }
}
