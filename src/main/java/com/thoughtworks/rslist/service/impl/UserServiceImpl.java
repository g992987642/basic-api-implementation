package com.thoughtworks.rslist.service.impl;


import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VotesRepository;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RsEventRepository rsEventRepository;
    private final VotesRepository votesRepository;

    @Override
    public void register(UserDto userDto) {
        UserEntity userEntity = CommonUtils.convertUserDtoToEntity(userDto);
        userRepository.save(userEntity);
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserEntity> allUserEntity = userRepository.findAll();
        List<UserDto> allUserDto = CommonUtils.convertUserEntityListToDtoList(allUserEntity);
        return allUserDto;
    }

    @Override
    public UserDto getUsersById(int id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return null;
        }
        UserDto userDto = CommonUtils.convertUserEntityToDto(userOptional.get());
        return userDto;
    }

    @Override
    public void deleteUsersById(int id) {

        userRepository.deleteById(id);

    }
}
