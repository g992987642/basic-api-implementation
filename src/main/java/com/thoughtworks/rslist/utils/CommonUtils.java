package com.thoughtworks.rslist.utils;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;

public  class CommonUtils {

    public static UserEntity convertUserDtoToEntity(UserDto userDto){

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

    public static RsEventEntity converRsDtoToEntity(RsEvent rsEvent){
        RsEventEntity rsEventEntity= RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .id(rsEvent.getUserId())
                .keyword(rsEvent.getKeyWord())
                .build();

        return rsEventEntity;
    }

}
