package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity register(@RequestBody @Valid UserDto userDto) {
        userService.register(userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() {
        List<UserDto> allUserDto = userService.getUsers();
        return ResponseEntity.ok().body(allUserDto);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUsersById(@PathVariable int id) {
        UserDto userDto = userService.getUsersById(id);
        if (userDto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(userDto);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUsersById(@PathVariable int id) {
        userService.deleteUsersById(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleException(MethodArgumentNotValidException ex) {
        CommentError commentError = new CommentError("invalid user");
        logger.error("[LOGGING]:" + commentError.getError());
        return ResponseEntity.badRequest().body(commentError);
    }


}
